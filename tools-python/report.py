#!/usr/bin/env python3
"""
Shopping Cart Reporting Utility
"""

import argparse, csv, collections, json, sys, time
from typing import Dict, List, Tuple

VALID_ITEMS = {"Apple": 35, "Banana": 20, "Melon": 50, "Lime": 15}

def melon_bogo_pay(n: int) -> int: return n // 2 + (n % 2)
def lime_three_for_two_pay(n: int) -> int: return (n // 3) * 2 + (n % 3)

def local_price(counts: Dict[str, int]) -> Tuple[Dict[str,int],Dict[str,int],int]:
    item_qty, total_per_item, total_price = {}, {}, 0
    for item, n in counts.items():
        if item not in VALID_ITEMS: continue
        unit = VALID_ITEMS[item]
        if item == "Melon": pay = melon_bogo_pay(n)
        elif item == "Lime": pay = lime_three_for_two_pay(n)
        else: pay = n
        line = pay * unit
        item_qty[item] = n
        total_per_item[item] = line
        total_price += line
    return item_qty, total_per_item, total_price

def call_java(java_base: str, items: List[str]):
    import requests
    url = java_base.rstrip("/") + "/price"
    try:
        r = requests.post(url, json={"items": items}, timeout=5)
        r.raise_for_status()
        data = r.json()
        return (
            data.get("itemQty") or data.get("qty") or {},
            data.get("totalPerItem") or data.get("lineTotals") or {},
            data.get("totalPrice") or data.get("totalPence") or 0
        )
    except Exception as e:
        return {}, {}, 0

def load_csv(path: str) -> Dict[str, List[str]]:
    carts = collections.defaultdict(list)
    with open(path, newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            cid, item = row["client_id"].strip(), row["item"].strip()
            carts[cid].append(item)
    return dict(carts)

def format_gbp(pence: int) -> str: return f"{pence/100:.2f}"

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--input", required=True, help="CSV with client_id,item")
    ap.add_argument("--java-base", help="Optional Java backend URL")
    args = ap.parse_args()

    carts = load_csv(args.input)
    summary = {"clients": {}, "totals": {"grandPence": 0, "grandGBP": "0.00"}}

    grand = 0
    for client, items in carts.items():
        valid = [i for i in items if i in VALID_ITEMS]
        counts = collections.Counter(valid)

        if args.java_base:
            item_qty, per_item, total = call_java(args.java_base, valid)
        else:
            item_qty, per_item, total = local_price(counts)

        summary["clients"][client] = {
            "items": items,
            "itemQty": item_qty,
            "totalPerItem": per_item,
            "totalPrice": total
        }
        grand += total

    summary["totals"]["grandPence"] = grand
    summary["totals"]["grandGBP"] = format_gbp(grand)

    # Pretty print
    print(json.dumps(summary, indent=2))

    # Save to file
    out_path = f"report_{int(time.time())}.json"
    with open(out_path, "w", encoding="utf-8") as f:
        json.dump(summary, f, indent=2)
    print(f"Saved report to {out_path}")

if __name__ == "__main__":
    main()

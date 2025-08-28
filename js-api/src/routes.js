const express = require('express');
const router = express.Router();
const cart = require('./cartStore');
const backend = require('./httpClient');
const VALID_ITEMS = new Set(["Apple", "Banana", "Melon", "Lime"]);

// Add item to cart
router.post('/cart/items', (req, res) => {
  const { item } = req.body || {};
  if (!item) return res.status(400).json({ error: 'item required' });

  if (!VALID_ITEMS.has(item)) {
    return res.status(400).json({
      error: 'invalid item',
      allowed: Array.from(VALID_ITEMS)
    });
  }

  const id = req.get('X-Client-Id') || 'default';
  cart.add(id, item);
  res.status(201).json({ ok: true, cart: cart.get(id) });
});

// View cart
router.get('/cart', (req, res) => {
  const id = req.get('X-Client-Id') || 'default';
  res.json({ items: cart.get(id) });
});

// Calculate total by calling Java backend
router.get('/cart/total', async (req, res, next) => {
  try {
    const id = req.get('X-Client-Id') || 'default';
    const items = cart.get(id);
    const breakdown = await backend.price(items);
    const total = breakdown.totalPrice;
    res.json({
      items,
      breakdown,
      totalPriceForAllItems: total
    });
  } catch (e) {
    next(e);
  }
});

module.exports = router;
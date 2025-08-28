const carts = new Map(); // key: clientId, value: array of items

exports.get = (id) => carts.get(id) || [];
exports.add = (id, item) => {
  const cart = carts.get(id) || [];
  cart.push(item);
  carts.set(id, cart);
};
exports.clear = (id) => carts.set(id, []);
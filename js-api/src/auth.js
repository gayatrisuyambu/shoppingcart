module.exports = function(req, res, next) {
  const apiKey = req.get('X-API-Key');
  const expected = process.env.API_KEY || 'secret123'; // default if not set

  if (!apiKey || apiKey !== expected) {
    return res.status(401).json({ error: 'Unauthorized: invalid API key' });
  }
  next();
};
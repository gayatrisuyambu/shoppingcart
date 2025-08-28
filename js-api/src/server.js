const express = require('express');
const app = express();

app.use(express.json());

const auth = require('./auth');
app.use('/api', auth, require('./routes'));

// mount routes
app.use('/api', require('./routes'));

// error handler
app.use((err, req, res, next) => {
  console.error('[ERROR]', err.message);
  res.status(500).json({ error: err.message });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`JS API running on ${PORT}`));
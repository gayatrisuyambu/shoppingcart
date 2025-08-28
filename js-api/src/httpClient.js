const axios = require('axios');

const JAVA_BASE = process.env.JAVA_BASE || 'http://localhost:8080/api';

exports.price = async (items) => {
  try {
    const { data } = await axios.post(`${JAVA_BASE}/price`, { items }, {
      headers: { 'Content-Type': 'application/json' },
      timeout: 5000
    });
    return data; // { qty, lineTotals, totalPence }
  } catch (err) {
    // Make backend errors easier to read
    const msg = err.response?.data?.error || err.message || 'Backend error';
    throw new Error(`Java backend failed: ${msg}`);
  }
};
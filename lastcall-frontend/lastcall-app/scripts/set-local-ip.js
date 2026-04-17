const os = require('os');
const fs = require('fs');
const path = require('path');

function getLocalIP() {
  const interfaces = os.networkInterfaces();
  for (const name of Object.keys(interfaces)) {
    for (const iface of interfaces[name]) {
      if (iface.family === 'IPv4' && !iface.internal) {
        return iface.address;
      }
    }
  }
  return null;
}

const ip = getLocalIP();
if (!ip) {
  console.error('Could not detect local IP address.');
  process.exit(1);
}

const envPath = path.join(__dirname, '..', '.env.local');
let content = '';

if (fs.existsSync(envPath)) {
  content = fs.readFileSync(envPath, 'utf-8');
  if (content.match(/^EXPO_PUBLIC_API_URL=.*/m)) {
    content = content.replace(/^EXPO_PUBLIC_API_URL=.*/m, `EXPO_PUBLIC_API_URL=http://${ip}:8080`);
  } else {
    content += `\nEXPO_PUBLIC_API_URL=http://${ip}:8080`;
  }
} else {
  content = `EXPO_PUBLIC_API_URL=http://${ip}:8080\n`;
}

fs.writeFileSync(envPath, content);
console.log(`API URL set to: http://${ip}:8080`);
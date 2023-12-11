import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.example.app',
  appName: 'Ad Id Example',
  webDir: 'dist',
  bundledWebRuntime: false,
  plugins: {
    'adjust-capacitor-plugin': {
        autoInit: false,
        appToken: 'qgpv5tkrnuo0',
        environment: 'sandbox',
        logLevel: 'verbose'
    }
}
};

export default config;

import { resolve } from 'node:path';
import { defineConfig } from 'vite';

export default defineConfig({
  root: 'src/main/resources/web',
  build: {
    outDir: resolve(import.meta.dirname, 'dist'),
    emptyOutDir: true
  }
});

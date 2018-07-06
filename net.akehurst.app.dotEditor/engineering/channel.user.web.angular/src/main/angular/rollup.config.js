import nodeResolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import typescript from 'rollup-plugin-typescript';
import angular from 'rollup-plugin-angular';

export default {
  input: 'src/main.ts',
  output: {
    name: 'dotEditor',
    file: 'dist/bundle.js',
    format: 'umd',
    sourceMap: true
  },
  plugins: [
    angular(),
    typescript({
      typescript: require('typescript')
    }),
    nodeResolve({ jsnext: true, main: true, browser: true }),
    commonjs()
  ]
}
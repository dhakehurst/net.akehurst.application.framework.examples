import nodeResolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import typescript from 'rollup-plugin-typescript2';
import angular from 'rollup-plugin-angular';
//import cleanup from 'rollup-plugin-cleanup';
import postcss from 'rollup-plugin-postcss-modules';

export default {
  input: 'src/main.ts',
  output: {
    name: 'dotEditor',
    file: 'dist/bundle.js',
    format: 'iife',
    sourceMap: true
  },
  plugins: [
    angular()
    ,typescript({
      typescript: require('typescript')
    })
    ,nodeResolve({ jsnext: true, main: true, browser: true })
    ,commonjs()
    ,postcss({
      
    })
    //,cleanup()
  ]
}
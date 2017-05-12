var webpackConfig = require('../../build/webpack.test.conf')

module.exports = function (config) {
  config.set({
    browsers: ['PhantomJS'], // Chrome, ChromeCanary, ...
    frameworks: ['mocha', 'sinon-chai'],
    reporters: ['spec', 'junit'],
    files: [
      '../../node_modules/es6-promise/dist/es6-promise.auto.js',
      '../../node_modules/babel-polyfill/dist/polyfill.js',
      './index.js'],
    preprocessors: {
      './index.js': ['webpack', 'sourcemap']
    },
    webpack: webpackConfig,
    webpackMiddleware: {
      noInfo: true
    },
    junitReporter: {
      outputDir: '../../../../../target/surefire-reports',
      outputFile: 'TEST-results.xml',
      useBrowserName: false
    }
  })
}

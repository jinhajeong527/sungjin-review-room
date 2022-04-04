const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: "../src/main/resources/static", //npm run build로 빌드 시 파일이 생성되는 위치
  indexPath: "../static/index.html", //index.html 파일 생기는 경로
  devServer: {
    proxy: "http://localhost:8080"
  }

})


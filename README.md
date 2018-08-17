# YouTuLibrary
封装腾讯优图插件，提供自定义接口，供jsp调用识别
依赖地址：	
dependencies {
	        implementation 'com.github.DevChangBo.YouTuLibrary:mvp:v1.0.7'
	}

如果失败，请在项目根目录的bulid下添加
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  第一个参数 action，给个默认参数，随便填，没有用到
  第二个参数 rawArgs、填写0-3，必填项
  0：表示身份证识别
  1：表示驾驶证识别
  2：表示行驶证识别
  3：表示车牌号识别

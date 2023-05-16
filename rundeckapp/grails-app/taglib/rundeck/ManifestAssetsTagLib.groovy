package rundeck

import groovy.json.JsonSlurper

class ManifestAssetsTagLib {
    def jsonSlurper = new JsonSlurper()

    def loadStyleFile = { attrs, body ->
        def serverUrl = new URL(grailsApplication.config.getProperty('grails.serverURL', String))
        def manifest = jsonSlurper.parse(new URL(serverUrl, attrs.manifest as String))
        def file = '/static/' + manifest[attrs.file as String]['file']
        out << asset.stylesheet(src: file)
    }
    def loadJsFile = { attrs, body ->
        def serverUrl = new URL(grailsApplication.config.getProperty('grails.serverURL', String))
        def manifest = jsonSlurper.parse(new URL(serverUrl, attrs.manifest as String))
        def file = '/static/' + manifest[attrs.file as String]['file']
        out << asset.javascript(src: file, 'asset-defer': attrs.getOrDefault('defer', false))
    }
    def loadAssetsFile = { attrs, body ->
        def serverUrl = new URL(grailsApplication.config.getProperty('grails.serverURL', String))
        def manifest = jsonSlurper.parse(new URL(serverUrl, attrs.manifest as String))
        def file = '/static/' + manifest[attrs.file as String]['file']
        if (manifest[attrs.file as String]['css']) {
            manifest[attrs.file as String]['css'].each { css ->
                {
                    def cssFile = '/static/' + css
                    out << asset.stylesheet(src: cssFile)
                }
            }
        }
        if (manifest[attrs.file as String]['imports']) {
            manifest[attrs.file as String]['imports'].each { jsImp ->
                {
                    def jsFile = '/static/' + manifest[jsImp as String]['file']
                    out << asset.javascript(src: jsFile, 'asset-defer': attrs.getOrDefault('defer', false))
                }
            }
        }
        out << asset.javascript(src: file, 'asset-defer': attrs.getOrDefault('defer', false))
    }
}

/*
 * Copyright 2017 Rundeck Inc, <http://rundeck.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rundeck.interceptors

import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import org.rundeck.app.access.InterceptorHelper

/**
 * Allows using HTTP headers to supply synchronizer tokens, they are injected in the parameters map before invoking
 * the controller, which may do withForm{} to validate them as parameters.
 */
class FormTokenInterceptor {

    public static final String TOKEN_KEY_HEADER = 'X-RUNDECK-TOKEN-KEY'
    public static final String TOKEN_URI_HEADER = 'X-RUNDECK-TOKEN-URI'

    int order = HIGHEST_PRECEDENCE + 2

    InterceptorHelper interceptorHelper

    FormTokenInterceptor() {
        matchAll().excludes(controller: 'static')
    }

    boolean before() {
        if(interceptorHelper.matchesAllowedAsset(controllerName, request)) return true
        //transfer request token from header to params, for the form verification used in controllers
        if(request.getHeader(TOKEN_KEY_HEADER) && request.getHeader(TOKEN_URI_HEADER)){
            params[SynchronizerTokensHolder.TOKEN_KEY]=request.getHeader(TOKEN_KEY_HEADER)
            params[SynchronizerTokensHolder.TOKEN_URI]=request.getHeader(TOKEN_URI_HEADER)
        }
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}

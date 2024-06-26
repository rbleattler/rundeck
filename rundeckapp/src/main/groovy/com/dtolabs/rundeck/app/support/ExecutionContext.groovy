/*
 * Copyright 2016 SimplifyOps, Inc. (http://simplifyops.com)
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

package com.dtolabs.rundeck.app.support

import grails.gorm.dirty.checking.DirtyCheck
import rundeck.support.filters.BaseNodeFilters

/**
 * ExecutionContext
 */
@DirtyCheck
abstract class ExecutionContext extends BaseNodeFilters{
    String project
    String argString
    String user
    String loglevel="WARN"
    String serverNodeUUID
    String timeout
    String retry
    String retryDelay

    static mapping = {
        user column: "rduser"
        argString type:'text'
        serverNodeUUID type:'text'
        timeout type:'text'
        retry type:'text'
        retryDelay type:'text'
    }
    static constraints = {
        retryDelay(nullable:true)
    }
    Boolean nodeKeepgoing=false
    Boolean doNodedispatch=false
    String nodeRankAttribute
    Boolean nodeRankOrderAscending=true
    Boolean nodeFilterEditable = false
}


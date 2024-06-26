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

package rundeck.support.filters

import com.dtolabs.rundeck.core.common.FrameworkResource
import grails.validation.Validateable

/*
 * ExtNodeFilters.java
 * 
 * User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 * Created: Jul 2, 2010 10:48:53 AM
 * $Id$
 */

/**
 * Extends BaseNodeFilters to add filter params used in GUI filtering
 */
public class ExtNodeFilters
        extends BaseNodeFilters
        implements Validateable {

    Long id
    Long version
    String project
    String filterName
    String filterExcludeName

    static constraints = {
        id (nullable: true)
        version (nullable: true)
        project(nullable: true, matches: FrameworkResource.VALID_RESOURCE_NAME_REGEX)
        filterName(nullable: true, matches: /^[^<>&'"\/]+$/)
        filterExcludeName(nullable: true, matches: /^[^<>&'"\/]+$/)
    }

    public boolean nodeFilterIsEmpty() {
        return super.nodeFilterIsEmpty()
    }

    public static from(BaseNodeFilters filters, String project) {
        def filters1 = new ExtNodeFilters()
        filters1.project = project
        filters1.filter = filters.asFilter()
        return filters1
    }
}

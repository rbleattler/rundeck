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

package rundeck

import org.junit.Test
import org.rundeck.storage.api.PathUtil

import static org.junit.Assert.assertEquals

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
class StorageTests {

    @Test
    void testGetPath() {
        assertEquals(PathUtil.asPath('abc'),new Storage(dir:'',name: 'abc').path)
        assertEquals(PathUtil.asPath('xyz/abc'),new Storage(dir:'xyz',name: 'abc').path)
        assertEquals(PathUtil.asPath('xyz/elf/abc'),new Storage(dir:'xyz/elf',name: 'abc').path)
    }

    @Test
    void testsetPath() {
        def s=new Storage()
        s.path='abc'
        assertEquals('abc',s.name)
        assertEquals('',s.dir)

        s.path='abc/xyz/monkey'
        assertEquals('monkey',s.name)
        assertEquals('abc/xyz',s.dir)
    }
}

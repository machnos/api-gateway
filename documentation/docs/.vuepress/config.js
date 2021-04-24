/*
 * Licensed to Machnos under one or more contributor license
 * agreements. Machnos licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

module.exports = {
    base: '/documentation/machnos-api-gateway/1.0.x/',
    title: 'Machnos Api Gateway - 1.0.x',
    description: 'Finding without searching',
    themeConfig: {
        logo: '/machnos-logo-512x512.png',
        smoothScroll: true,
        nav: [
            {text: 'Home', link: 'https://www.machnos.com/'},
            {text: 'Downloads', link: 'https://www.machnos.com/downloads/'}
        ],
        sidebarDepth: 2,
        sidebar: [
            '/',
            {
                title: 'Error codes',
                path: '/error-codes/',
            },
            {
                title: 'Changelog',
                path: '/changelog/',
            },
        ]
    }
};

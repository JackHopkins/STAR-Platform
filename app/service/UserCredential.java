/**
 * Copyright 2014 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
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
 *
 */
package service;

import securesocial.core.BasicProfile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserCredential implements Serializable {
	
    private static final long serialVersionUID = 1L;

    public BasicProfile main;
    public List<BasicProfile> identities;
    
    public UserCredential(BasicProfile user) {
        this.main = user;
        identities = new ArrayList<>();
        identities.add(user);
    }
    
    @Override
    public String toString() {
        return this.main.firstName().get() + " " + this.main.lastName().get() + " " + this.main.email().get();
    }
    
}

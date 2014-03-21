/*
 * Copyright 2014 Y12STUDIO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.blackbananacoin.tools.privkeyshare;

public class UserObj {

	private String id;
	private String keyPublic;
	private String keyPrivate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyPublic() {
		return keyPublic;
	}

	public void setKeyPublic(String keyPublic) {
		this.keyPublic = keyPublic;
	}

	public String getKeyPrivate() {
		return keyPrivate;
	}

	public void setKeyPrivate(String keyPrivate) {
		this.keyPrivate = keyPrivate;
	}

}

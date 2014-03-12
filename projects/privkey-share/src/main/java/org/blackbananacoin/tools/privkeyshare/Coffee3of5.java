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

import java.math.BigInteger;
import java.util.List;
import com.google.bitcoin.core.Utils;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class Coffee3of5 {
	
	public final static String TEST_ADDR1 = "1Co35VWMwPufEyFWXzXBSU3XWQg5L1gd4e";

	public static void main(String[] args) {
		// block 288168
		// hash =
		// "00000000000000008dc5f7922f63497150e10657ebdd8b3b8b8274d86646d773";
		String block288168hash = "00000000000000008dc5f7922f63497150e10657ebdd8b3b8b8274d86646d773";
		String[] strs = { "hello1", "hello2", "hello3", "hello4", "hello5",
				block288168hash };
		List<BigInteger> bl = Lists.newArrayList();
		for (int i = 0; i < strs.length; i++) {
			BigInteger b1 = new BigInteger(1, Utils.doubleDigest(strs[i]
					.getBytes()));
			bl.add(b1);
			System.out.println(b1);
		}

		BigInteger x = Ordering.natural().max(bl);
		System.out.println(x);
	}

}

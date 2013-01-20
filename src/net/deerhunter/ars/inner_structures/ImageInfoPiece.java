/*
 * This file is part of Android retrieval system project.
 * 
 * Android retrieval system is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. 
 * 
 * Android retrieval system is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Android retrieval system. If not, see <http://www.gnu.org/licenses/>.
 */

package net.deerhunter.ars.inner_structures;

/**
 * Class that contains a main information about image.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class ImageInfoPiece {
	private int storageId;

	public ImageInfoPiece(int storageId) {
		this.storageId = storageId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageInfoPiece other = (ImageInfoPiece) obj;
		if (storageId != other.storageId)
			return false;
		return true;
	}

	/**
	 * Returns storage ID of the image
	 * 
	 * @return Storage ID of the image
	 */
	public int getStorageId() {
		return storageId;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer(100);
		buffer.append("ImageInfoPiece {");
		buffer.append(" storageId = " + storageId);
		buffer.append(" }");
		return buffer.toString();
	}
}

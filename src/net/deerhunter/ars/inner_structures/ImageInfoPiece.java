package net.deerhunter.ars.inner_structures;

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

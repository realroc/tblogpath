package com.test.zp.nio.http;

import java.nio.ByteBuffer;

/**
 * @author tcowan
 */
public class FileDescriptor {
	public long size;
	public String contentType;
	public long lastModified;
	public ByteBuffer data;
}

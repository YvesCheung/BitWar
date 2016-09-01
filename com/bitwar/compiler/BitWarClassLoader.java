package com.bitwar.compiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;

import com.bitwar.util.Log;

public class BitWarClassLoader extends ClassLoader {
	private String basedir; // 需要该类加载器直接加载的类文件的基目录

	public BitWarClassLoader(String basedir) {
		this.basedir = basedir;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		String fileName = name.replace(".", File.separator) + ".class";
		File classFile = new File(basedir, fileName);
		if (!classFile.exists()) {
			throw new ClassNotFoundException(classFile.getPath() + " 不存在");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteBuffer bf = ByteBuffer.allocate(1024);
		FileInputStream fis = null;
		FileChannel fc = null;
		try {
			fis = new FileInputStream(classFile);
			fc = fis.getChannel();
			while (fc.read(bf) > 0) {
				bf.flip();
				bos.write(bf.array(), 0, bf.limit());
				bf.clear();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return defineClass(bos.toByteArray(), 0, bos.toByteArray().length);
	}
}

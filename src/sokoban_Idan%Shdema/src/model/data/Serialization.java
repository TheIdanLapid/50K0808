package model.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Reads and writes to objects by serialization.
 * @author שדמה
 *
 */
public class Serialization {

		//de-serialize to Object from given file
	public static Object readSer(InputStream is) throws IOException,
		ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(is);
		Object obj = ois.readObject();
		ois.close();
		return obj;
	}

		//serialize the given object and save it to file
	public static void writeSer(Object obj, String fileName)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);

		fos.close();
	}
}

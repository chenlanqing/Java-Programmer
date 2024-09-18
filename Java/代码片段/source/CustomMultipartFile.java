import org.springframework.web.multipart.MultipartFile;
import java.io.*;

/**
 * 自定义MultipartFile的实现类
 */
public class CustomMultipartFile implements MultipartFile {

    private final byte[] content;
    private final String filename;
    private final String contentType;

    public CustomMultipartFile(byte[] content, String filename, String contentType) {
        this.content = content;
        this.filename = filename;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return filename;
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream out = new FileOutputStream(dest)) {
            out.write(content);
        }
    }

    @Override
    public String toString() {
        return "CustomMultipartFile[fileName=\"" + this.filename + "\"" +
                (this.getContentType() != null ? ", contentType=" + this.getContentType() : "") +
                ", size=" + this.getSize() + "]";
    }
}

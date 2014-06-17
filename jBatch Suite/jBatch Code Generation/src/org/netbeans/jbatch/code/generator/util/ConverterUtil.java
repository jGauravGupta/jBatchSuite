/**
 * Copyright [2014] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.jbatch.code.generator.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.editor.BaseDocument;
import org.netbeans.jbatch.code.templates.ClassPathLoader;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

public class ConverterUtil {

    private static final String JAVA_EXTENSION = ".java";

    public static File createFile(String parentDir, String childDir,
            String fileName) throws IOException {

        if (childDir == null) {
            return createFile(parentDir, fileName);
        }

        childDir = childDir.replaceAll("\\.", "\\" + File.separatorChar);
        File dir = new File(parentDir, childDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    public static File createFile(String parentDir, String fileName)
            throws IOException {

        File dir = new File(parentDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    public static byte[] getBytes(File file) throws IOException {

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        if (file.length() > Integer.MAX_VALUE) {
            throw new IOException("file is too large for single read");
        }

        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;

        try {

            fileInputStream = new FileInputStream(file);
            int read = fileInputStream.read(bytes);

            if (read != file.length()) {
                throw new IOException("could not read entire file");
            }

        } finally {
            fileInputStream.close();
        }

        return bytes;
    }

    public static String getContent(File file) throws IOException {
        return new String(getBytes(file));
    }

    public static String getContent(File file, final String charsetName) throws IOException {
        return new String(getBytes(file), charsetName);
    }

    public static void writeContent(String content, File file)
            throws IOException {

        writeContent(content, Charset.defaultCharset().toString(), file);
    }

    public static void writeContent(String content, String charset, File file)
            throws IOException {

        if (file.isDirectory()) {
            throw new IOException("Cannot write content to directory");
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes(charset));
        fos.close();
    }

    public static void formatFile(File file) {
        final FileObject fo = FileUtil.toFileObject(file);
        try {
            DataObject dobj = DataObject.find(fo);
            EditorCookie ec = dobj.getLookup().lookup(EditorCookie.class);
            if (ec == null) {
                return;
            }
            ec.close();
            StyledDocument document = ec.getDocument();
            if (document instanceof BaseDocument) {
                final BaseDocument doc = (BaseDocument) document;
                final Reformat f = Reformat.get(doc);
                f.lock();
                try {
                    doc.runAtomic(new Runnable() {
                        public void run() {
                            try {
                                f.reformat(0, doc.getLength());
                            } catch (BadLocationException ex) {
                                Exceptions.attachMessage(ex, "Failure while formatting " + FileUtil.getFileDisplayName(fo));
                                Exceptions.printStackTrace(ex);
                            }

                        }
                    });
                } finally {
                    f.unlock();
                }
                try {
                    ec.saveDocument();
//                    SaveCookie save = dobj.getLookup().lookup(SaveCookie.class);
//                    if (save != null) {
//                        save.save();
//                    }
                } catch (IOException ex) {
                    Exceptions.attachMessage(ex, "Failure while formatting and saving " + FileUtil.getFileDisplayName(fo));
                    Exceptions.printStackTrace(ex);
                }
            }
        } catch (DataObjectNotFoundException ex) {
            Exceptions.attachMessage(ex, "Failure while formatting " + FileUtil.getFileDisplayName(fo));
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.attachMessage(ex, "Failure while formatting " + FileUtil.getFileDisplayName(fo));
            Exceptions.printStackTrace(ex);
        }
    }

    public static void generateFile(String templateURI, String destinationDir, String _package, String named, String name) {
        try {
            Configuration cfg = new Configuration();
            cfg.setClassForTemplateLoading(ClassPathLoader.class, "");
            Template template = cfg.getTemplate(templateURI);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("package", _package);
            data.put("named", named);
            data.put("name", name);
            File sourceFile = ConverterUtil.createFile(destinationDir, _package, name + JAVA_EXTENSION);
            Writer out = new FileWriter(sourceFile);
            template.process(data, out);
            out.flush();
            formatFile(sourceFile);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TemplateException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}

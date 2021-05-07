package net.casheh.celllevel.util;

import com.google.common.collect.Table;
import net.casheh.calls.CallBackAPI;
import net.casheh.celllevel.CellLevel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DependencyUtils {

    private CellLevel plugin;

    public DependencyUtils(CellLevel plugin) {
        this.plugin = plugin;
    }

    public void loadDepends() {
        File libs = new File(this.plugin.getDataFolder(), "libs");
        if (!libs.exists())
            libs.mkdir();
        try {
            File smartInvs = new File(this.plugin.getDataFolder(), "libs/SmartInvs-1.2.7.jar");
            if (!smartInvs.exists())
                download(new URL("https://repo1.maven.org/maven2/fr/minuskube/inv/smart-invs/1.2.7/smart-invs-1.2.7.jar"), "SmartInvs-1.2.7.jar");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void downloadDependency(URL url, File fileName, CallBackAPI<Double> callback) {
        BufferedInputStream in = null;
        FileOutputStream out = null;
        try {
            URLConnection conn = url.openConnection();
            int size = conn.getContentLength();
            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(fileName);
            byte[] data = new byte[1024];
            double sumCount = 0.0D;
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
                sumCount += count;
                if (size > 0) {
                    double porcentage = sumCount / size * 100.0D;
                    callback.done(Double.valueOf(porcentage));
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            if (out != null)
                try {
                    out.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
        }
    }

    private void download(URL url, String name) {
        File libraries = new File(this.plugin.getDataFolder(), "libs");
        if (!libraries.exists())
            libraries.mkdir();
        File fileName = new File(libraries, name + ".jar");
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
                downloadDependency(url, fileName, value -> {
                    if (value.doubleValue() >= 100.0D) {
                        loadJarFile(fileName);
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            loadJarFile(fileName);
        }
    }

    private void loadJarFile(File jar) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Class<?> getClass = classLoader.getClass();
            Method method = getClass.getSuperclass().getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(classLoader, new Object[] { jar.toURI().toURL() });
        } catch (NoSuchMethodException|MalformedURLException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}

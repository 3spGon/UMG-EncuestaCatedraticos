/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umg.encuestas.bean;

import com.umg.encuesta.util.ArchivosCarga;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author NESPINOZA
 */
public class FileUploadView implements Serializable{

    private UploadedFile file;
    private String destino = "/home/rgiron/Descargas/temp/";
    List<ArchivosCarga> archivos = new LinkedList<ArchivosCarga>();
    private String NOM_ARCH = "";
    private String nuevodestino = "";
    private String nuevonomarchi = "";
    private String extension = "";

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
//            leerExcel();
        }
    }

    public void fileuploaded(FileUploadEvent event) {
        System.out.println("fileuploaded");
        if (archivos.isEmpty()) {
System.out.println("if1");
            ArchivosCarga archivo = new ArchivosCarga();
            archivo.setArchivo(event.getFile());
            String encodedURL = event.getFile().getFileName();
            extension = encodedURL.substring(encodedURL.lastIndexOf(".") + 1, encodedURL.length());
            archivo.setNombre(encodedURL);
            archivos.add(archivo);

            if (uploadAllFiles()) {
System.out.println("if2");
                for (ArchivosCarga archivox : archivos) {
                    System.out.println("for1");
                    File f = new File(destino + NOM_ARCH);

                    if (extension.equals("xlsx")) {
                        System.out.println("iffor1");
                        leerExcel(f);
                    }
                }
            }

        }

    }

    private boolean uploadAllFiles() {
        boolean allUploaded = false;

        int contadorSubidas = 0;
        for (ArchivosCarga bean : archivos) {

            boolean subido = uploadFile(bean.getArchivo());
            System.out.println("subido " + subido);
            if (subido) {
                contadorSubidas++;
            }

        }
        if (contadorSubidas == archivos.size()) {
            allUploaded = true;
        }

        return allUploaded;
    }

    private boolean uploadFile(UploadedFile uploadedFile) {
        // String fileName = uploadedFile.getFileName();

        FacesContext context = FacesContext.getCurrentInstance();
        LoginBean objLogin = (LoginBean) context.getExternalContext().getSessionMap().get("loginBean");
        SimpleDateFormat sf3 = new SimpleDateFormat("ddMMyyyyHHmmss");
        String fileName = objLogin.getNombre() + sf3.format(new Date()) + "." + extension;
        boolean uploaded = false;
        try {
            InputStream in = uploadedFile.getInputstream();
// write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destino + fileName));
            nuevodestino = destino + fileName;
            nuevonomarchi = fileName;
            NOM_ARCH = fileName;
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            uploaded = true;

        } catch (IOException e) {
            System.out.println("error cargando archivo");

        }

        return uploaded;
    }

    public void leerExcel(File rutaArchivo) {
       // String nombreArchivo = this.file.getFileName();
       // String rutaArchivo = "/Users/local/temp/" + nombreArchivo;
        String hoja = "Simple";

        try (FileInputStream file = new FileInputStream(rutaArchivo)) {
            // leer archivo excel
            XSSFWorkbook worbook = new XSSFWorkbook(file);
            //obtener la hoja que se va leer
            XSSFSheet sheet = worbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel
            Iterator<Row> rowIterator = sheet.iterator();

            Row row;
            // se recorre cada fila hasta el final
            int y = 0;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                //se obtiene las celdas por fila
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell cell;
                //se recorre cada celda
                int x = 0;
                while (cellIterator.hasNext()) {
                    // se obtiene la celda en específico y se la imprime
                    if (x == 0 && y >= 4) {
                        cell = cellIterator.next();
                        System.out.print(cell.getNumericCellValue() + " | ");
                    } else {
                        cell = cellIterator.next();
                        System.out.print(cell.getStringCellValue() + " | ");
                    }

                    x++;
                }
                System.out.println();
                y++;
            }
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
            e.getMessage();
        }
    }
}

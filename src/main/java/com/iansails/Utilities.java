package com.iansails;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.persistence.TypedQuery;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * http://viralpatel.net/blogs/20-useful-java-code-snippets-for-java-developers/
 * @author : ian
 * @date : 1/26/14
 */
public class Utilities {

    private static final Logger log = LoggerFactory.getLogger(Utilities.class);

    /**
     * Get a single result from typed query or return null. Not sure why no result is an exception case
     *  Though now that i think about it.. this would be an easier way to return useful feedback to the caller like..
     *      HEY YOU AREN'T LOGGED IN
     * @param query
     * @param <T>
     * @return
     */
    public static <T> T getOne(TypedQuery<T> query) {
        T t = null;
        try {
            t = query.getSingleResult();
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            //this is not an error case!
        } catch (IncorrectResultSizeDataAccessException exception) {
            log.warn("IncorrectResultSize");
        }
        return t;
    }

    public static <T> List<T> list(TypedQuery<T> query) {
        List<T> t = null;
        try {
            t = query.getResultList();
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            t = new ArrayList<T>();
        }
        return t;
    }

//    public void postMail( String recipients[ ], String subject, String message , String from) throws MessagingException
//    {
//        boolean debug = false;
//
//        //Set the host smtp address
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.example.com");
//
//        // create some properties and get the default Session
//        Session session = Session.getDefaultInstance(props, null);
//        session.setDebug(debug);
//
//        // create a message
//        Message msg = new MimeMessage(session);
//
//        // set the from and to address
//        InternetAddress addressFrom = new InternetAddress(from);
//        msg.setFrom(addressFrom);
//
//        InternetAddress[] addressTo = new InternetAddress[recipients.length];
//        for (int i = 0; i < recipients.length; i++)
//        {
//            addressTo[i] = new InternetAddress(recipients[i]);
//        }
//        msg.setRecipients(Message.RecipientType.TO, addressTo);
//
//
//        // Optional : You can also set your custom headers in the Email if you Want
//        msg.addHeader("MyHeaderName", "myHeaderValue");
//
//        // Setting the Subject and Content Type
//        msg.setSubject(subject);
//        msg.setContent(message, "text/plain");
//        Transport.send(msg);
//    }


    private void createThumbnail(String filename, int thumbWidth, int thumbHeight, int quality, String outFilename)
            throws InterruptedException, FileNotFoundException, IOException
    {
        // load image from filename
        Image image = Toolkit.getDefaultToolkit().getImage(filename);
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);
        mediaTracker.waitForID(0);
        // use this to test for errors at this point: System.out.println(mediaTracker.isErrorAny());

        // determine thumbnail size from WIDTH and HEIGHT
        double thumbRatio = (double)thumbWidth / (double)thumbHeight;
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        double imageRatio = (double)imageWidth / (double)imageHeight;
        if (thumbRatio < imageRatio) {
            thumbHeight = (int)(thumbWidth / imageRatio);
        } else {
            thumbWidth = (int)(thumbHeight * imageRatio);
        }

        // draw original image to thumbnail image object and
        // scale it to the new size on-the-fly
        BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

        // save thumbnail image to outFilename
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFilename));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
        quality = Math.max(0, Math.min(quality, 100));
        param.setQuality((float)quality / 100.0f, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(thumbImage);
        out.close();
    }

    //fast file copy using NIO
    public static void fileCopy( File in, File out )
            throws IOException
    {
        FileChannel inChannel = new FileInputStream( in ).getChannel();
        FileChannel outChannel = new FileOutputStream( out ).getChannel();
        try
        {
//          inChannel.transferTo(0, inChannel.size(), outChannel);      // original -- apparently has trouble copying large files on Windows

            // magic number for Windows, 64Mb - 32Kb)
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while ( position < size )
            {
                position += inChannel.transferTo( position, maxCount, outChannel );
            }
        }
        finally
        {
            if ( inChannel != null )
            {
                inChannel.close();
            }
            if ( outChannel != null )
            {
                outChannel.close();
            }
        }
    }
}


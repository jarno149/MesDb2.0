/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarno.rajala.mesdb.filelistener;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 *
 * @author jarno
 */
public class FileWatcher implements Runnable
{
    private WatchService watchService;
    private FileListener fileListener;
    private long lastInvoke;

    public FileWatcher(WatchService watchService, FileListener fileListener)
    {
        this.watchService = watchService;
        this.fileListener = fileListener;
        this.lastInvoke = System.currentTimeMillis();
    }
    
    @Override
    public void run()
    {
        try
        {
            WatchKey key = this.watchService.take();
            while(key != null)
            {
                for(WatchEvent event : key.pollEvents())
                {
                    String fileName = event.context().toString();
                    String compareFilename = this.fileListener.getFilename();
                    if(this.fileListener.isListenAllFilesInDir())
                    {
                        long compareTimeStamp = System.currentTimeMillis();
                        if(this.lastInvoke + this.fileListener.getMinDelayBetweenInvokes() < compareTimeStamp)
                        {
                            if(this.fileListener.isListenAllFilesInDir())
                            {
                                this.lastInvoke = compareTimeStamp;
                                this.fileListener.getActions().onEvent(fileName);
                            }
                            else
                            {
                                if(compareFilename.equals(fileName))
                                {
                                    this.lastInvoke = compareTimeStamp;
                                    this.fileListener.getActions().onEvent(fileName);
                                }
                            }
                        }
                    }
                }
            }
            key.reset();
            key = this.watchService.take();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        System.err.println("Filelistener thread stopped!");
    }
    
}

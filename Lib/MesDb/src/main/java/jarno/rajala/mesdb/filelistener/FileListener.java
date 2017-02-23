/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarno.rajala.mesdb.filelistener;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

/**
 *
 * @author jarno
 */
public class FileListener
{
    private Path folderPath;
    private String filename;
    private FileListenerActions actions;
    private boolean listenAllFilesInDir;
    private int minDelayBetweenInvokes;
    
    private Thread listenerThread;

    public FileListener(String folderPath, String filename, FileListenerActions actions)
    {
        this.folderPath = Paths.get(folderPath);
        this.filename = filename;
        this.actions = actions;
    }
    
    public void startListener()
    {
        try
        {
            WatchService watchService = folderPath.getFileSystem().newWatchService();
            FileWatcher watcher = new FileWatcher(watchService, this);
            this.listenerThread = new Thread(watcher);
            this.listenerThread.start();
            System.out.println("Listener-thread started.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void stopListener()
    {
        if(this.listenerThread != null)
        {
            this.listenerThread.interrupt();
            this.listenerThread = null;
        }
    }
    
    public boolean isListening()
    {
        if(this.listenerThread != null)
        {
            return this.listenerThread.isAlive();
        }
        return false;
    }

    public FileListenerActions getActions()
    {
        return actions;
    }

    public boolean isListenAllFilesInDir()
    {
        return listenAllFilesInDir;
    }

    public void setListenAllFilesInDir(boolean listenAllFilesInDir)
    {
        this.listenAllFilesInDir = listenAllFilesInDir;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setMinDelayBetweenInvokes(int minDelayBetweenInvokes)
    {
        this.minDelayBetweenInvokes = minDelayBetweenInvokes;
    }

    public int getMinDelayBetweenInvokes()
    {
        return minDelayBetweenInvokes;
    }
}

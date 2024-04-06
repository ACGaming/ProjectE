package moze_intel.projecte.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import moze_intel.projecte.PECore;

public class ThreadCheckUUID extends Thread
{
    private static final String UUID_FILE_PATH = "/haUUID.txt";
    private static boolean hasRunServer = false;
    private static boolean hasRunClient = false;

    public static boolean hasRunServer()
    {
        return hasRunServer;
    }

    public static boolean hasRunClient()
    {
        return hasRunClient;
    }

    private final boolean isServerSide;

    public ThreadCheckUUID(boolean isServer)
    {
        this.isServerSide = isServer;
        this.setName("ProjectE UUID Checker " + (isServer ? "Server" : "Client"));
    }

    @Override
    public void run()
    {
        try (InputStream inputStream = getClass().getResourceAsStream(UUID_FILE_PATH); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            if (inputStream == null)
            {
                PECore.LOGGER.fatal("UUID file not found!");
                throw new IOException("UUID file not found in resources!");
            }

            String line;
            List<String> uuids = new ArrayList<>();

            while ((line = reader.readLine()) != null)
            {
                if (!line.isEmpty() && !line.startsWith("###UUID"))
                {
                    uuids.add(line);
                }
            }

            PECore.uuids.addAll(uuids);
        }
        catch (IOException e)
        {
            PECore.LOGGER.fatal("Caught exception in UUID Checker thread!");
            e.printStackTrace();
        }
        finally
        {
            if (isServerSide)
            {
                hasRunServer = true;
            }
            else
            {
                hasRunClient = true;
            }
        }
    }
}

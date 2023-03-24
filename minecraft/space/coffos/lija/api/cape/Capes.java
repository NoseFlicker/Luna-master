package space.coffos.lija.api.cape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Capes {

    private static Map<String, ResourceLocation> capes = new HashMap<>();

    public static void loadCape(String ID) {
        String url = null;
        try {
            url = "http://lunaclient.pw/api/cape/" + ID + ".png";
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ResourceLocation resourceLocation = new ResourceLocation("capes/" + ID + ".png");
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

        IImageBuffer iImageBuffer = new IImageBuffer() {
            @Override
            public BufferedImage parseUserSkin(BufferedImage var1) {
                return var1;
            }

            @Override
            public void func_152634_a() {
                capes.put(ID, resourceLocation);
            }
        };

        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, iImageBuffer);
        textureManager.loadTexture(resourceLocation, threadDownloadImageData);
    }

    public static void deleteCape(String ID) {
        capes.remove(ID);
    }

    public static ResourceLocation getCape(String ID) {
        return capes.getOrDefault(ID, null);
    }

    public static boolean hasCape(String ID) {
        return capes.containsKey(ID);
    }
}
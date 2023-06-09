package space.coffos.lija.impl.gui;

import net.minecraft.client.renderer.texture.TextureUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public class LiJAFont {

    private int IMAGE_WIDTH = 1024;
    private int IMAGE_HEIGHT = 1024;
    private int texID;
    private final IntObject[] chars = new IntObject[2048];
    private final Font font;
    private int fontHeight = -1;
    private int charOffset;

    LiJAFont(final Font font, final boolean antiAlias, final int charOffset) {
        this.font = font;
        this.charOffset = charOffset;
        setupTexture(antiAlias);
    }

    private void setupTexture(final boolean antiAlias) {
        if (font.getSize() <= 15) {
            IMAGE_WIDTH = 256;
            IMAGE_HEIGHT = 256;
        }
        if (font.getSize() <= 43) {
            IMAGE_WIDTH = 512;
            IMAGE_HEIGHT = 512;
        } else if (font.getSize() <= 91) {
            IMAGE_WIDTH = 1024;
            IMAGE_HEIGHT = 1024;
        } else {
            IMAGE_WIDTH = 2048;
            IMAGE_HEIGHT = 2048;
        }

        BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setFont(font);

        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setColor(Color.white);

        int rowHeight = 0;
        int positionX = 0;
        int positionY = 0;
        for (int i = 0; i < 2048; i++) {
            final char ch = (char) i;
            final BufferedImage fontImage = getFontImage(ch, antiAlias);

            final IntObject newIntObject = new IntObject();

            newIntObject.width = fontImage.getWidth();
            newIntObject.height = fontImage.getHeight();

            if (positionX + newIntObject.width >= IMAGE_WIDTH) {
                positionX = 0;
                positionY += rowHeight;
                rowHeight = 0;
            }

            newIntObject.storedX = positionX;
            newIntObject.storedY = positionY;

            if (newIntObject.height > fontHeight)
                fontHeight = newIntObject.height;

            if (newIntObject.height > rowHeight)
                rowHeight = newIntObject.height;
            chars[i] = newIntObject;
            g.drawImage(fontImage, positionX, positionY, null);

            positionX += newIntObject.width;
        }

        try {
            texID = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), img, true, true);
        } catch (final NullPointerException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage getFontImage(final char ch, final boolean antiAlias) {
        final BufferedImage temporaryFontImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = (Graphics2D) temporaryFontImage.getGraphics();
        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        else
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setFont(font);
        final FontMetrics fontMetrics = g.getFontMetrics();
        int charwidth = fontMetrics.charWidth(ch) + 8;

        if (charwidth <= 0)
            charwidth = 7;
        int charheight = fontMetrics.getHeight() + 3;
        if (charheight <= 0)
            charheight = font.getSize();
        final BufferedImage fontImage = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D gt = (Graphics2D) fontImage.getGraphics();
        if (antiAlias)
            gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        else
            gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        gt.setFont(font);
        gt.setColor(Color.WHITE);
        final int charx = 3;
        final int chary = 1;
        gt.drawString(String.valueOf(ch), charx, chary + fontMetrics.getAscent());

        return fontImage;

    }

    private void drawChar(final char c, final float x, final float y) throws ArrayIndexOutOfBoundsException {
        try {
            drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void drawQuad(final float x, final float y, final float width, final float height, final float srcX, final float srcY, final float srcWidth, final float srcHeight) {
        final float renderSRCX = srcX / IMAGE_WIDTH, renderSRCY = srcY / IMAGE_HEIGHT, renderSRCWidth = srcWidth / IMAGE_WIDTH, renderSRCHeight = srcHeight / IMAGE_HEIGHT;
        glBegin(GL_TRIANGLES);
        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        glVertex2d(x + width, y);
        glTexCoord2f(renderSRCX, renderSRCY);
        glVertex2d(x, y);
        glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        glVertex2d(x, y + height);
        glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        glVertex2d(x, y + height);
        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        glVertex2d(x + width, y + height);
        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        glVertex2d(x + width, y);
        glEnd();
    }

    public void drawString(final String text, double x, double y, final Color color, final boolean shadow) {
        x *= 2;
        y = y * 2 - 2;
        glPushMatrix();
        glScaled(0.25D, 0.25D, 0.25D);
        TextureUtil.bindTexture(texID);
        glColor(shadow ? new Color(0.05F, 0.05F, 0.05F, color.getAlpha() / 255F) : color);
        final int size = text.length();
        for (int indexInString = 0; indexInString < size; indexInString++) {
            final char character = text.charAt(indexInString);
            if (character < chars.length) {
                drawChar(character, (float) x, (float) y);
                x += chars[character].width - charOffset;
            }
        }
        glPopMatrix();
    }

    private void glColor(final Color color) {
        final float red = color.getRed() / 255F, green = color.getGreen() / 255F, blue = color.getBlue() / 255F, alpha = color.getAlpha() / 255F;
        glColor4f(red, green, blue, alpha);
    }

    int getStringHeight(final String text) {
        int lines = 1;
        for (char c : text.toCharArray())
            if (c == '\n')
                lines++;
        return (fontHeight - charOffset) / 2 * lines;
    }

    public int getHeight() {
        return (fontHeight - charOffset) / 2;
    }

    public int getStringWidth(final String text) {
        int width = 0;
        for (final char c : text.toCharArray())
            if (c < chars.length)
                width += chars[c].width - charOffset;
        return width / 2;
    }

    public Font getFont() {
        return font;
    }

    private static class IntObject {
        public int width;
        public int height;
        int storedX;
        int storedY;
    }
}
package space.coffos.lija.impl.gui.clickgui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.elements.luna.render.hud.HUD;
import space.coffos.lija.impl.managers.FontManager;
import space.coffos.lija.util.entity.PlayerUtils;
import space.coffos.lija.util.general.dynamic.DynamicVariables;
import space.coffos.lija.util.math.MathUtils;
import space.coffos.lija.util.render.ColorUtils;
import space.coffos.lija.util.render.RenderUtils;
import space.coffos.lija.util.render.TransitionUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ClickGui extends GuiScreen {

    private ArrayList<SettingButton> settings = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();
    private static boolean showDescription;
    private Category selected_category;
    private Element selected_element;
    private boolean adding, adding_s;
    private ScaledResolution sr;
    private static int a;
    private int w = 100;

    /*
        Runs When The ClickGui Opens.
          (Adds Buttons To Array)
     */
    @Override
    public void initGui() {
        sr = PlayerUtils.getScaledRes();
        initCategorys();
        if (selected_element == null) w = 100;
        initParts(selected_element != null);
        super.initGui();
    }


    private void initCategorys() {
        /*
           The Starting Point for The Buttons Cords
           Some Quick Math: Spacing will equal 2 & Category Size is 5
           So X = Screen Width / 2 - (2.5 * 52) 2.5 Because There Is 2
           Buttons And A Half Button And The 50 Because That Is There
           Width.

           This Will Make The Buttons Centered.
         */
        double x = (width >> 1) - 2.5 * (52 * sr.getScaleFactor() >> 1);
        /*
            Creates New Instance Of The Array On Open To
            Prevent More Than One Object.
         */
        buttons = new ArrayList<>();
        /*
            Adding All Categorys To The Buttons Array
                 Also Setting The Buttons Args.
                (A For Loop Grabbing Each One)
         */
        for (Category c : Category.values()) {
            buttons.add(new Button(c, x, (height >> 1) - (100 * sr.getScaleFactor() >> 1), 52 * sr.getScaleFactor() / 2, 20, this));
            x += 52 * sr.getScaleFactor() >> 1;
        }
    }


    /*
       Draws The ClickGui Parts And Frame.
               (Kind Of Obv)
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        TransitionUtils transition = new TransitionUtils();
        super.drawScreen(mouseX, mouseY, partialTicks);
        DynamicVariables.initVar("c:y", (height >> 1) + (w + 50 * sr.getScaleFactor() >> 1), true, false);
        Gui.drawRect((width >> 1) - (130 * sr.getScaleFactor() >> 1), (height >> 1) - (100 * sr.getScaleFactor() >> 1), (width >> 1) + (130 * sr.getScaleFactor() >> 1), (height >> 1) + (w * sr.getScaleFactor() >> 1), new Color(41, 41, 41).getRGB());
        Gui.drawRect((width >> 1) - (130 * sr.getScaleFactor() >> 1), (height >> 1) - (100 * sr.getScaleFactor() >> 1), (width >> 1) - (60 * sr.getScaleFactor() >> 1), (height >> 1) + (w * sr.getScaleFactor() >> 1), new Color(31, 31, 31).getRGB());
        a = transition.transitionTo(0, 255, 3);
        if (showDescription)
            Gui.drawRect((width >> 1) - (130 * sr.getScaleFactor() >> 1), (height >> 1) + (w + 50 * sr.getScaleFactor() >> 1), (width >> 1) + (130 * sr.getScaleFactor() >> 1), (height >> 1) + (w + 64 * sr.getScaleFactor() >> 1), new Color(31, 31, 31, a > 255 ? 255 : a).getRGB());
        else {
            transition.resetTransition(false);
            a = 0;
        }
        showDescription = false;
        // Making A For Loop With Lambda To Call The Draw Method For Each Button.
        buttons.forEach(b -> b.draw(mouseX, mouseY));
        settings.forEach(s -> s.draw(mouseX, mouseY));
        if (adding | adding_s) {
            initParts(!adding);
            adding = false;
            adding_s = false;
        }
    }

    /*
                    Called When Mouse Clicked.
            (Used To Get When A Element / Category Is Clicked)
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        // Making A For Loop With Lambda To Call The Clicked Method For Each Button.
        buttons.forEach(b -> b.clicked(mouseX, mouseY, mouseButton));
        settings.forEach(SettingButton::clicked);
    }


    /*
        A Method Which Adds And Settings And
            Or Elements To The Arrays
     */
    private void initParts(boolean setting) {
        if (!setting) {
            if (selected_category != null) {
                initCategorys();
                //A Starting Point For The Buttons On The Y Axis
                int y = height / 2 - 80 * sr.getScaleFactor() / 2;
                //Adding All The New Buttons
                if (LiJA.INSTANCE.elementManager.getElementsForCategory(selected_category) != null) {
                    for (Element e : LiJA.INSTANCE.elementManager.getElementsForCategory(selected_category)) {
                        if (e.isCompatible()) {
                            buttons.add(new Button(e, (width >> 1) - (130 * sr.getScaleFactor() >> 1), y, 70, 15, this));
                            y += 16;
                        }
                    }
                }
            }
        } else {
            if (selected_element != null & LiJA.INSTANCE.settingManager.getSettingsByElement(selected_element) != null) {
                settings = new ArrayList<>();
                //A Starting Point For The Buttons On The Y Axis
                int y = height / 2 - 80 * sr.getScaleFactor() / 2;
                //Adding All The New Buttons
                for (int i = 0; i < LiJA.INSTANCE.settingManager.getSettingsByElement(selected_element).size(); i++) {
                    Setting s = LiJA.INSTANCE.settingManager.getSettingsByElement(selected_element).get(i);
                    int x = width / 2 - 50 * sr.getScaleFactor() / 2; // w = 120;
                    if (i == 11) {
                        y = height / 2 - 80 * sr.getScaleFactor() / 2;
                    }
                    if (i >= 11) {
                        x = width / 2 + 40 * sr.getScaleFactor() / 2;
                    }
                    settings.add(new SettingButton(s, x, y, 70, 15));
                    y += 16;
                }
            } else
                w = 100;
        }
    }

    /*
        A Method Called When The Mouse Button Is Released
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        settings.forEach(SettingButton::released);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        buttons.forEach(b -> b.key(keyCode));
    }

    /*
        A New Class For The Buttons That Are Drawn.
           ( Category And Element Buttons)
     */
    public static class Button {
        private double x, y, width, height;
        private Type type;
        private Element element;
        private Category category;
        private boolean hovered, binding;
        private ClickGui parent;

        /*
             Gets The Info For The Buttons And Sets The Variables
                 Above With The Info That It Was Sent.
                      ( x, y, width, height etc)
          */
        public Button(Category category, double x, double y, int width, int height, ClickGui parent) {
            this.category = category;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.type = Type.CATEGORY;
            this.parent = parent;
        }

        public Button(Element element, double x, double y, int width, int height, ClickGui parent) {
            this.element = element;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.type = Type.ELEMENT;
            this.parent = parent;
        }


        /*
            A public void Which Is Called In drawScreen With its Args.
                (mouseX & mouseY For Hover State)
         */
        public void draw(int mouseX, int mouseY) {
            hovered = mouseX >= x & mouseY >= y & mouseX <= x + width & mouseY <= y + height;
            /*
                Gets The Button Type To Determine What To Do Next.
                              (From The Enum)
             */
            if (type.equals(Type.CATEGORY)) {
                Gui.drawRect(x, y, x + width, y + height, new Color(31, 31, 31).getRGB());
                if (parent.selected_category != null & Objects.equals(parent.selected_category, category)) {
                    double transitionReach = x + width - 2, transitionStart = x + 2;
                    TransitionUtils transition = new TransitionUtils();
                    int rainbow = RenderUtils.getRainbow(3000, 15, 0.5f).getRGB();
                    Gui.drawRect(transitionStart, y + height - 1, transition.transitionTo((float) transitionStart, (float) transitionReach, 2.55f), y + height, transitionReach - (float) transition.getTransition(true) == 0 ? HUD.rainbowArrayList.getValBoolean() ? rainbow : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN) : rainbow);
                }
                FontManager.bebasMedium.drawCenteredString(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase(), (int) (x + width / 2), (int) (y + height / 2 - FontManager.bebasMedium.getHeight() / 2), -1);
            }
            if (type.equals(Type.ELEMENT)) {
                if (hovered) {
                    showDescription = !element.getDescription().isEmpty();
                    // To prevent a flashing alpha bug, only start showing the text with a transition after [a] is greater or equal to 4. a -> transition alpha value
                    if (a >= 4)
                        FontManager.comfortaaMedium.drawString(element.getDescription(), (int) x + 2, (int) DynamicVariables.pullVar("c:y", false) + 2, new Color(255, 255, 255, a).getRGB(), false); // FIXME: Flashing alpha
                    Gui.drawRect(x, y, x + width, y + height, new Color(29, 29, 29).getRGB());
                }
                Gui.drawRect(x + width - 4, y + 1, x + width - 2, y + height - 1, element.isToggled() ? ColorUtils.getColor(ColorUtils.ColorChoices.MAIN) : new Color(15, 15, 15).getRGB());

                FontManager.bebasStandardGUI.drawString(binding ? "Bind: " + Keyboard.getKeyName(element.getKeyCode()).toUpperCase() : element.getName(), (int) x + 5, (int) (y + height / 2 - FontManager.comfortaaMedium.getHeight() / 2) - 2, 0x90ffffff, false);
            }
        }

        /*
            A public void Which Is Called In mouseClicked With Its Args.
              (mouseX, mouseY & button Its Obvious What They Are For)
         */
        public void clicked(int mouseX, int mouseY, int button) {
            if (!hovered) return;
                /*
                Gets The Button Type To Determine What To Do Next.
                              (From The Enum)
                 */
            if (type.equals(Type.CATEGORY)) {
                /*
                    Sets The Selected Category To The Buttons
                    Category And Calls The Setup Parts Method.
                 */
                if (parent.selected_category == category) return;
                ((TransitionUtils) DynamicVariables.pullVar("Transition", false)).resetTransition(true);
                parent.selected_category = category;
                parent.adding = true;
                parent.settings.clear();
                parent.selected_element = null;
            }
            if (type.equals(Type.ELEMENT)) {
                /*
                    Finds Out What Type Of Click It Was:
                    If Its Left Toggle The Element.
                    If Its Right Check If The Element Has Settings
                    Then If It Does Set The Selected Element To It
                    And Then It Calls The Setup Parts Method.

                 */
                if (button == 0)
                    element.toggle();
                if (button == 1 && LiJA.INSTANCE.settingManager.getSettingsByElement(element) != null) {
                    parent.selected_element = element;
                    parent.adding_s = true;
                }
                if (button != 2) return;
                binding = !binding;
            }

        }

        /*
            A Method Called When A Key Is Pressed
         */
        public void key(int keyCode) {
            if (!binding) return;
            element.setKeyCode(Keyboard.isKeyDown(Keyboard.KEY_DELETE) ? 0 : keyCode);
            binding = false;
        }

        public Type getType() {
            return type;
        }

        /*
            A Enum Which Tells The Button If It
            is A Category Button Or A Module Button
        */
        public enum Type {
            CATEGORY, ELEMENT, NONE
        }
    }

    /*
              A New Class For The Settings Buttons That Are Drawn.
                  ( Slider, Checkbox and Mode Buttons)
      */
    public class SettingButton {

        private Setting s;
        private double x, y, width, height;
        private boolean dragging, hovered;

        /*
            Gets The Info For The Buttons And Sets The Variables
                Above With The Info That It Was Sent.
                     ( x, y, width, height etc)
         */
        SettingButton(Setting s, int x, int y, int width, int height) {
            this.s = s;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /*
          A public void Which Is Called In drawScreen With its Args.
              (mouseX & mouseY For Hover State)
       */
        public void draw(int mouseX, int mouseY) {
            hovered = (mouseX >= x & mouseY >= y & mouseX <= x + width & mouseY <= y + height);
            if (s.isCheck()) {
                FontManager.bebasStandardGUI.drawString(s.getName(), (float) ((int) x - 2), (int) (y + height / 2 - FontManager.bebasStandardGUI.getHeight() / 2), hovered ? 0x90ffffff : 0x50ffffff, false);
                FontManager.bebasStandardGUI.drawString(s.getValBoolean() ? "On" : "Off", (int) (x + width - FontManager.bebasStandardGUI.getStringWidth(s.getValBoolean() ? "ON" : "OFF")), (int) (y + height / 2 - FontManager.bebasStandardGUI.getHeight() / 2), hovered ? 0x90ffffff : 0x50ffffff, false);
            }
            if (s.isCombo() & !s.isLockedMode())
                FontManager.bebasStandardGUI.drawString(s.getName() + ": " + s.getValString(), (int) (x + FontManager.bebasStandardGUI.getStringWidth("") - width / 35), (int) (y + height / 2 - FontManager.bebasStandardGUI.getHeight() / 2), hovered ? 0x90ffffff : 0x50ffffff, false);
            if (!(s.isSlider() & !s.isLockedDouble())) return;
            double pos = (this.width - 8) * (s.getValDouble() - s.getMin()) / (s.getMax() - s.getMin());
            Gui.drawRect(x + 4, y + height - 5, x + width - 4, y + height - 1, new Color(12, 12, 12).getRGB());
            Gui.drawRect(x + 4, y + height - 5, x + 4 + (pos), y + height - 1, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
            FontManager.bebasStandardGUI.drawCenteredString(s.getName() + ": " + s.getValDouble(), (int) (this.x + width / 2), (int) this.y - 1, hovered ? 0x90ffffff : 0x50ffffff);
            if (dragging) {
                double difference = Math.min(width - 0, Math.max(0, mouseX - x));
                if (difference == 0) {
                    s.setValDouble(s.getMin());
                    return;
                }
                s.setValDouble(this.s.getMax() <= 10 ? MathUtils.roundToPlace((((difference) / (width)) * (s.getMax() - s.getMin())) + s.getMin(), 1) : Math.round(MathUtils.roundToPlace((((difference) / (width)) * (s.getMax() - s.getMin())) + s.getMin(), 2)));
            }
        }

        /*
          A public void Which Is Called In mouseClicked With Its Args.
            (mouseX, mouseY & button Its Obvious What They Are For)
         */
        public void clicked() {
            if (!hovered) return;
            if (s.isCheck()) s.setValBoolean(!s.getValBoolean());
            if (s.isCombo()) try {
                s.setValString(s.getOptions().get(s.currentIndex() + 1));
            } catch (Exception e) {
                s.setValString(s.getOptions().get(0));
            }
            if (!s.isSlider()) return;
            dragging = !dragging;
        }

        /*
              A public void Which Is Called In mouseReleased this
              is used to tell the slider that its no longer dragged
         */
        public void released() {
            dragging = false;
        }
    }
}
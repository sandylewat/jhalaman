/*
 * Basic morphology transformation implementation
 */
package jhalaman.image.filters;

import java.util.Arrays;
import jhalaman.image.Citra;
import jhalaman.ui.component.SimpleViewer;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class Morphology {

    Boolean[] kernel;
    int height;
    int width;
    int size;
    Boolean[] in;

    public Morphology(Boolean[] kernel) {
        if (kernel.length != 9) {
            kernel[0] = true;
            kernel[1] = true;
            kernel[2] = true;
            kernel[3] = true;
            kernel[4] = true;
            kernel[5] = true;
            kernel[6] = true;
            kernel[7] = true;
            kernel[8] = true;

        } else {
            this.kernel = kernel;
        }
    }

    public Morphology() {
        kernel = new Boolean[9];
        kernel[0] = true;
        kernel[1] = true;
        kernel[2] = true;
        kernel[3] = true;
        kernel[4] = true;
        kernel[5] = true;
        kernel[6] = true;
        kernel[7] = true;
        kernel[8] = true;

    }

    public void erode(Citra citra, int totalRun) {
        height = citra.getHeight();
        width = citra.getWidth();
        size = height * width;
        in = citra.toBoolean();

        for (int i = 0; i < width; ++i) {
            in[i] = false;
            in[i + width] = false;
            in[size - 1 - i - width] = false;
            in[size - 1 - i] = false;
        }

        for (int i = 1; i < height - 1; ++i) {
            int j = i * width;
            in[j] = false;
            in[j + 1] = false;
            in[j - 1] = false;
            in[j - 1] = false;
        }

        int i = 0;
        do {
            erodeOnce();
            ++i;

        } while (i < totalRun);
        citra.setVal(in);
    }

    public int getTotalErosion(Citra citra) {
        height = citra.getHeight();
        width = citra.getWidth();
        size = height * width;
        in = citra.toBoolean();


        for (int i = 0; i < width; ++i) {
            in[i] = false;
            in[i + width] = false;
            in[size - 1 - i - width] = false;
            in[size - 1 - i] = false;
        }

        for (int i = 1; i < height - 1; ++i) {
            int j = i * width;
            in[j] = false;
            in[j + 1] = false;
            in[j - 1] = false;
            in[j - 1] = false;
        }

        int deleted;
        int i = 0;
        do {
            deleted = erodeOnce();
            ++i;

        } while (deleted != 0);

        return i;
    }

    private int erodeOnce() {
        int deleted = 0;
        int i = size - width - 2;
        Boolean[] out = Arrays.copyOf(in, size);
        while (i-- > 2 * width) {
            if (i % width > 1 && i % width < width - 2) {
                if (in[i]) {
                    Boolean keep = true;
                    if (kernel[0]) {
                        keep = keep && in[i - width - 1];
                    }
                    if (kernel[1]) {
                        keep = keep && in[i - width];
                    }
                    if (kernel[2]) {
                        keep = keep && in[i - width + 1];
                    }
                    if (kernel[3]) {
                        keep = keep && in[i - 1];
                    }
                    if (kernel[5]) {
                        keep = keep && in[i + 1];
                    }
                    if (kernel[6]) {
                        keep = keep && in[i + width - 1];
                    }
                    if (kernel[7]) {
                        keep = keep && in[i + width];
                    }
                    if (kernel[8]) {
                        keep = keep && in[i + width + 1];
                    }

                    if (!keep) {
                        out[i] = false;
                        ++deleted;
                    }
                }
            }
        }
        in = out;
        return deleted;
    }

    public void dilate(Citra citra, int totalRun) {
        height = citra.getHeight();
        width = citra.getWidth();
        size = height * width;
        in = citra.toBoolean();

        for (int i = 0; i < width; ++i) {
            in[i] = false;
            in[i + width] = false;
            in[size - 1 - i - width] = false;
            in[size - 1 - i] = false;
        }

        for (int i = 1; i < height - 1; ++i) {
            int j = i * width;
            in[j] = false;
            in[j + 1] = false;
            in[j - 1] = false;
            in[j - 1] = false;
        }

        SimpleViewer sv;
        int i = 0;
        do {
            dilateOnce();
            ++i;

        } while (i < totalRun);
        citra.setVal(in);
    }

    private void dilateOnce() {
        int i = size - width - 2;
        Boolean[] out = Arrays.copyOf(in, size);
        while (i-- > 2 * width) {
            if (i % width > 1 && i % width < width - 2) {
                if (!in[i]) {
                    Boolean add = false;
                    if (kernel[0]) {
                        add = add || in[i - width - 1];
                    }
                    if (kernel[1]) {
                        add = add || in[i - width];
                    }
                    if (kernel[2]) {
                        add = add || in[i - width + 1];
                    }
                    if (kernel[3]) {
                        add = add || in[i - 1];
                    }
                    if (kernel[5]) {
                        add = add || in[i + 1];
                    }
                    if (kernel[6]) {
                        add = add || in[i + width - 1];
                    }
                    if (kernel[7]) {
                        add = add || in[i + width];
                    }
                    if (kernel[8]) {
                        add = add || in[i + width + 1];
                    }

                    if (add) {
                        out[i] = true;
                    }
                }
            }
        }
        in = out;
    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class LinkedListImage implements CompressedImageInterface {

    private class Node {
        public int index =   -1;
        public Node next = null;

        public Node(int index) {
            this.index = index;
        }

        public Node() { ; }

        public String toString() { return Integer.toString(index); }
    }

    private Node[] image;
    private int height;
    private int width;

    private void constructor(boolean[][] grid, int width, int height) {
        image = new Node[height];

        for (int i = 0; i < height; i++) {
            image[i] = new Node();

            int start = 0, end = 0;
            boolean getend   = true;
            boolean getstart = false;

            for (int j = width-1; j > -1; j--) {

                if (!grid[i][j] &&   getend) {
                    end   = j;
                    getend = false; 
                    getstart = true;
                }

                if (grid[i][j] && getstart) {
                    start = j+1;
                    getstart = false; 
                }

                if (j == 0 && getstart) {
                    start = j;
                    getstart = false;
                }
                
                if (!getend && !getstart) 
                {
                    Node en = new Node(end);
                    en.next = image[i];
                    Node sn = new Node(start);
                    sn.next = en;

                    image[i] = sn;
                    getend = true;
                    getstart = false;
                }
            }
        }

        this.height = height;
        this.width  = width;
    }

    public LinkedListImage(String filename) throws FileNotFoundException
    {
        File file = new File(filename);
        Scanner s = new Scanner(file);

        int height, width;
        width  = s.nextInt();
        height = s.nextInt();
        s.nextLine();

        boolean[][] grid = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = (s.nextInt() != 0);
            }
            s.nextLine();
        }

        this.constructor(grid, width, height);
    }

    public LinkedListImage(boolean[][] grid, int width, int height)
    {
        constructor(grid, width, height);
    }

    public boolean getPixelValue(int x, int y) throws PixelOutOfBoundException
    {
        Node node = image[x];
        int n = 1;

        if (x > height || y > width) { throw new PixelOutOfBoundException("Pixel coordinate out of range."); }

        while (node.next != null) {
            int start = node.index;
            node = node.next;
            int end = node.index;
            node = node.next;

            if      (y <  start && y >= n  ) { return  true; }
            else if (y >= start && y <= end) { return false; }

            n = end + 1;
        }

        return true;
    }

    public void setPixelValue(int x, int y, boolean val)
    {
        //you need to implement this
        throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public int[] numberOfBlackPixels()
    {
        int[] ret = new int[this.height];

        for (int i = 0; i < this.height; i++) {
            Node node = this.image[i];
            int n = 0;

            while (node.next != null) {
                int start =  node.index;
                node = node.next;
                int end = node.index;
                node = node.next;

                n = n + end - start + 1;
            }

            ret[i] = n;
        }

        return ret;
    }
    
    public void invert()
    {
        //you need to implement this
        throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public void performAnd(CompressedImageInterface img)
    {
        //you need to implement this
        throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public void performOr(CompressedImageInterface img)
    {
        //you need to implement this
        throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public void performXor(CompressedImageInterface img)
    {
        //you need to implement this
        throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public String toStringUnCompressed()
    {
        String output = new String();

        output = output + Integer.toString(this.width) + ' ' + Integer.toString(this.height) + ", ";
        
        for (int i = 0; i < this.height; i++) {
            String line  = new String();
            Node node = image[i];
            int n = 0;
            int start, end = 0;

            while (node.next != null) {
                start = node.index;
                node = node.next;
                end = node.index;
                node = node.next;

                for (int j = n;   j < start; j++) { line = line + "1 "; }
                for (int j = start; j < end; j++) { line = line + "0 "; }

                n = end;
            }

            for (int k = end; k < width; k++) { line = line + "1 "; }

            if (line != null && line.length() > 0) { line = line.substring(0, line.length() - 1); }
            output = output + line + ", ";
        }

        return output.substring(0, output.length() - 2);
    }
    
    public String toStringCompressed()
    {
        String output = new String();
        output = output + Integer.toString(this.width) + ' ' + Integer.toString(this.height) + ", ";
        
        for (int i = 0; i < this.height; i++) {
            String line  = new String();
            Node node = image[i];
            
            while (node.next != null) {
                line = line + Integer.toString(node.index) + ' ';
                node = node.next;
            }
            line = line + "-1";
            output = output + line + ", ";
        }

        return output.substring(0, output.length() - 2);
    }

    public static void main(String[] args) throws FileNotFoundException, PixelOutOfBoundException {
        // testing all methods here :
        boolean success = true;

        // check constructor from file
        CompressedImageInterface img1 = new LinkedListImage("C:\\Users\\Sumit\\IdeaProjects\\DS-assignment-2\\src\\sampleInputFile.txt");

        // check toStringCompressed
        String img1_compressed = img1.toStringCompressed();
        String img_ans = "16 16, -1, 5 7 -1, 3 7 -1, 2 7 -1, 2 2 6 7 -1, 6 7 -1, 6 7 -1, 4 6 -1, 2 4 -1, 2 3 14 15 -1, 2 2 13 15 -1, 11 13 -1, 11 12 -1, 10 11 -1, 9 10 -1, 7 9 -1";
        success = success && (img_ans.equals(img1_compressed));

        if (!success)
        {
            System.out.println("Constructor (file) or toStringCompressed ERROR");
            return;
        }

        // check getPixelValue
        boolean[][] grid = new boolean[16][16];
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    grid[i][j] = img1.getPixelValue(i, j);                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }

        // check constructor from grid
        CompressedImageInterface img2 = new LinkedListImage(grid, 16, 16);
        String img2_compressed = img2.toStringCompressed();
        success = success && (img2_compressed.equals(img_ans));

        if (!success)
        {
            System.out.println("Constructor (array) or toStringCompressed ERROR");
            return;
        }

        System.out.print(img1.toStringUnCompressed());

/*      // check Xor
        try
        {
            img1.performXor(img2);       
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && (!img1.getPixelValue(i,j));                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }

        if (!success)
        {
            System.out.println("performXor or getPixelValue ERROR");
            return;
        }

        // check setPixelValue
        for (int i = 0; i < 16; i++)
        {
            try
            {
                img1.setPixelValue(i, 0, true);            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }*/

        // check numberOfBlackPixels
        int[] img1_black = img1.numberOfBlackPixels();
        success = success && (img1_black.length == 16);
        for (int i = 0; i < 16 && success; i++)
            success = success && (img1_black[i] == 15);
        if (!success)
        {
            System.out.println("setPixelValue or numberOfBlackPixels ERROR");
            return;
        }

        /*// check invert
        img1.invert();
        for (int i = 0; i < 16; i++)
        {
            try
            {
                success = success && !(img1.getPixelValue(i, 0));            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }
        if (!success)
        {
            System.out.println("invert or getPixelValue ERROR");
            return;
        }

        // check Or
        try
        {
            img1.performOr(img2);        
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && img1.getPixelValue(i,j);
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performOr or getPixelValue ERROR");
            return;
        }

        // check And
        try
        {
            img1.performAnd(img2);    
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && (img1.getPixelValue(i,j) == img2.getPixelValue(i,j));             
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performAnd or getPixelValue ERROR");
            return;
        }*/

        // check toStringUnCompressed
        String img_ans_uncomp = "16 16, 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1, 1 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1, 1 1 1 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1 1, 1 1 0 0 0 1 1 1 1 1 1 1 1 1 1 1, 1 1 0 0 1 1 1 1 1 1 1 1 1 1 0 0, 1 1 0 1 1 1 1 1 1 1 1 1 1 0 0 0, 1 1 1 1 1 1 1 1 1 1 1 0 0 0 1 1, 1 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1, 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1, 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1 1, 1 1 1 1 1 1 1 0 0 0 1 1 1 1 1 1";
        success = success && (img1.toStringUnCompressed().equals(img_ans_uncomp)) && (img2.toStringUnCompressed().equals(img_ans));

        if (!success)
        {
            System.out.println("toStringUnCompressed ERROR");
            return;
        }
        else
            System.out.println("ALL TESTS SUCCESSFUL! YAYY!");
    }
}
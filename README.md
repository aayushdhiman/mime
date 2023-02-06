# MIME

## Design Changes
Our design changed slightly, as we changed how the interfaces interacted with each other. In order to implement the additional commands (blur, sepia, sharpen, and greyscale), we decided to use a command design, as was practiced in Lab 6. This allowed us to have minimal changes to existing code. In reference to the commands, currently, sepia and greyscale are complete. 

We felt that we could justify the minimal changes that were made to the PPMModel file because not only did we not actually remove functionality from it, but we added an extra method. If someone else were using the PPMModel code, then their existing code would still function, even after the additional method we added was implemented. 

## Design Implementations
I created the filter with more implementations in mind-I built an abstract class that has a 
method applyFilter, which each class that extends the abstract class Filter is able to use. By 
calling applyFilter, on an image, one is able to apply the filter to each pixel of the image. 
One can also create the 2D double array in the constructor of the class that extends the 
abstract class. 

The color transformation macro commands are reliant on the abstract class AbstractMacro class. 
This gives them the necessary methods to perform matrix multiplication as well as RGBcapping 
of the color values.
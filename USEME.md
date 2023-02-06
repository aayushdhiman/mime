# MIME

## Supported Script Commands.

As shown in the welcome message of the program, there are multiple commands that are supported by the program. These commands work for conventional filetypes (ppm, png, jpg, and bmp), examples of which are provided.
The following is a list of commands and how to enter them:

load filepath refname - loads an image from filepath and calls it refname
save filepath refname - saves an image called refname to filepath
red-component refname refname2 - greyscales an image called refname using its red component and calls it refname2
green-component refname refname2 - greyscales an image called refname using its green component and calls it refname2
blue-component refname refname2 - greyscales an image called refname using its blue component and calls it refname2
value-component refname refname2 - greyscales an image called refname using its value component and calls it refname2
intensity-component refname refname2 - greyscales an image called refname using its intensity component and calls it refname2
luma-component refname refname2 - greyscales an image called refname using its luma component and calls it refname2
horizontal-flip refname refname2 - flips an image called refname horizontally and calls it refname2
vertical-flip refname refname2 - flips an image called refname vertically and calls it refname2
brighten increment refname refname2 - brightens an image called refname by increment, or darkens it if increment is negative, and calls it refname2
greyscale refname refname2 - greyscales an image called refname and calls it refname2
blur refname refname2 - blurs an image called refname and calls it refname2
sharpen refname refname2 - sharpens an image called refname and calls it refname2
sepia refname refname2 - sharpens an image called refname and calls it refname2
q - quits the program

## Conditions
An image must be loaded before any changes can happen to it.
Saves only occur after the program has been quit.

## Examples
load res/6ColorRGB.png six

red-component six sixRed

save res/6Red.png sixRed

q

This will load the png 6ColorRGB, greyscale it using its red component, and then save it as 6Red.png

load res/b.ppm b

green-component b bGreen

blue-component b bBlue

value-component b bValue

intensity-component bIntensity

luma-component bLuma

greyscale b bGrey

save res/b.png bGrey

q

This will load the image b.ppm, greyscale it multiple times, then save the last one as a png.

load res/6ColorRGB.jpg six

horizontal-flip six sixH

vertical-flip sixH sixHV

brighten 100 sixHV sixHVB

blur sixHV sixHVBlur

sharpen sixHV sixHVS

sepia sixHV sixHVSepia

save res/sixHVSepia.ppm sixHVSepia

q

This will load the image 6ColorRGB.jpg, flip it twice, brighten it, then blur, sharpen, and sepia it, then save the sepia as a ppm.

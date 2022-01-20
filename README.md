# TODO Kotlin/Native ![Build Status](https://travis-ci.org/twbs/bootstrap-rubygem.svg?branch=master)
This TODO project is an easy and customizable tool which will help you organize your time. Application runs fully in terminal and was built with Linux in mind.

## Installation
Download .kexe file from releases. Before launching make sure that this location exists: ~/.config/todo/. If it doesn't - create all folders that are missing.

## Key binding
Default key binding:
* <kbd>Q</kbd> - exits program
* <kbd>INSERT</kbd> - Creates new header / checkbox
* <kbd>BACKSPACE</kbd> - Deletes header / checkbox. In edit mode deletes last letter
* <kbd>ENTER</kbd> - Expands header
* <kbd>ENTER</kbd> - Change state of checkbox. It saves text in edit mode
* <kbd>E</kbd> - Enter edit mode of line you are pointing on

## Customization
If default configuration doesn't suit you, you can always change it. All you need to do is to make 'config' file in "~/.config/todo/config".
Every line in your config file should look like that: "variable_name value". At first, you write variable's name, then you make space, and you write value of variable.
Here's every command listed:

### Key Binding
* up - move cursor up
* down - move cursor down
* left - move cursor left
* right - move cursor right
* use - expand header or change state of checkbox
* exit - exits application
* new - creates new checkbox/header
* remove - removes/deletes header of checkbox you are pointing on
* edit - enters edit mode of line you are pointing on
#### Key codes
As value of any key bind you can write any character from a-z or A-Z, additional keycodes are:
* INSERT
* BACKSPACE
* SPACE
* TAB
* ENTER

### Formatting
* indent_spaces - indentation of checkbox
* spaces - number of spaces between state of checkbox and name of checkpoint

## Save file
Application automatically saves everything to "~/.config/todo/save". It was designed to be easily readable for humans, so you can edit it manually if you want. Example save file:
```
#Example Header
[ ] Example 'TODO' checkbox
[-] Example 'Doing' checkbox
[X] Example 'Done' checkbox
#Example Header 2
[ ] Example 'TODO' checkbox
[-] Example 'Doing' checkbox
[X] Example 'Done' checkbox
```
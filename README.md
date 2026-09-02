## Solocards: New and modern way to manage flashcards

Solocards is a lightweight and offline-first flashcard app, which focuses on a simplicity and smooth user experience. This application has several features that makes it special:

### 1. Quiz manager
Quiz is just a list of flashcards. There are the ways to manage quizzes:
* Select favorite quizzes
* Sort them by name and date
* Search quizzes
* Archive/unarchive quizzes
* Move quizzes to trash
* Filter quizzes by tags
* Import/Export quiz as JSON file
* Share quiz

### 2. Fast editor
* Create cards in seconds
* Smooth flip and transition animations
* Modify quiz attributes (such as "Show answer when incorrect" or "Shuffle cards")
* Specify all incorrect options of the card (for the Option Game Mode)
* Set how many options to display on a screen
* Specify all modes for this card that will appear in Mixed Mode (all modes in one place)

### 3. Game modes
* **Flip mode:** Figure out the answer in your head, then check yourself by answering "Sure", "Maybe", "Not sure"
* **Writing mode:**: Write the answer from a digital keyboard
* **Boolean mode:** Answer TRUE or FALSE depending on a fact
* **Option mode:** Choose the right option
* **Mixed mode:** All modes together

### 4. History feature
* At the end of the test, rate your result from 1 to 5
* Show the history of all your sessions
* See the card count, time, date and result of previous sessions

### 5. ⭐ Scripting feature
The core feature of Solocards. Imagine you want to make a quiz that teaches you a multiplication table. Normally, you need to create over 50 cards, which is quite exhausting. So, instead of writing "What is 2×3", "What is 3×4", "What is 5×7" etc, you can write a single card with a folowing question: "What is {A}×{B}", where A and B are random numbers. The answer side of this card is just "{C}", where C is a product of these 2 numbers. Also, using this way, you can generate random options for Option Mode. This feature is completely optional and doesn't require programming skills. [More details at the bottom...](#scripting-tutorial)

### 6. Cloud storage
* Fully optional feature
* Allows you storing quizzes and game history in the cloud. Useful to synchonize your data between multiple devices.

### 7. In-app tutorial
Easy for beginners to use

---

## Scripting tutorial

As mentioned before, it's an optional feature that helps you generating more cards. The programming language is Lua but with built-in functions for better experience

### Example #1

Make sure your question and answer is the same as below:

**Question:** `What is {a} + {b}?` **Answer:** `{c}`

```lua
a = rand(1, 10)
b = rand(1, 10)
c = a*b
```

Function `rand(min, max)` generates random integer in the inclusive range.
Function `randExcept(min, max, except)` excludes the number in the range.
Now this card generate random addition tasks.

### Example #2

**Question:** `What is {a} - {b}?` **Answer:** `{c}`

And also add these options to option list:
`{op1}`, `{op2}`, `{op3}`

```lua
a = rand(1, 10)
b = rand(1, 10)
c = a-b

-- Generates 3 different options from -10 to 10 inclusively and excluding the answer (the value of c variable)
op1, op2, op3 = randOptions(-10, 10, 3, c)
```

### Example #3

**Question:** `Find the {taskType} of {a} and {b}?` **Answer:** `{c}`

```lua
a = rand(1, 10)
b = rand(1, 10)

-- The pick() function returns random item from the list
taskType = pick({"min", "max", "average"})

if taskType == "min" then
  c = math.min(a, b)
elseif taskType == "max" then
  c = math.max(a, b)
else
  c = (a + b) / 2
end
```

import os
from PIL import Image

out_dir = 'PolyLoveMarble/app/src/main/res/drawable'
os.makedirs(out_dir, exist_ok=True)

# Process the 6 newly uploaded individual dice faces!
for i in range(1, 7):
    p = f'uploads/Dice {i}.jpeg'
    if os.path.exists(p):
        img = Image.open(p).convert('RGBA')
        pixels = img.load()
        w, h = img.size
        for x in range(w):
            for y in range(h):
                r, g, b, a = pixels[x, y]
                # Key out the pure black background corners
                if r < 15 and g < 15 and b < 15:
                    pixels[x, y] = (r, g, b, 0)
        img.save(f'{out_dir}/gothic_dice_{i}.png', 'PNG')
        print(f"Generated transparent gothic_dice_{i}.png from Dice {i}.jpeg!")
    else:
        print(f"Error: {p} not found!")

print("--- NEW DICE FACES PROCESSED SUCCESSFULLY ---")

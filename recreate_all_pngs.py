import os
from PIL import Image

out_dir = 'PolyLoveMarble/app/src/main/res/drawable'
os.makedirs(out_dir, exist_ok=True)

# 1. Gothic Dice (Using your new individual Dice 1 to 6!)
for i in range(1, 7):
    p = f'uploads/Dice {i}.jpeg'
    if os.path.exists(p):
        img = Image.open(p).convert('RGBA')
        pixels = img.load()
        w, h = img.size
        for x in range(w):
            for y in range(h):
                r, g, b, a = pixels[x, y]
                # Key out pure black background corners
                if r < 15 and g < 15 and b < 15:
                    pixels[x, y] = (r, g, b, 0)
        img.save(f'{out_dir}/gothic_dice_{i}.png', 'PNG')
    else:
        print(f"Error: {p} not found!")
print("New Dice faces generated completely!")

# 2. Setup Header (from uploads!)
header_src = 'uploads/copilot_image_1784396047533.jpeg'
if os.path.exists(header_src):
    img = Image.open(header_src)
    banner = img.crop((0, 0, img.width, 480)).convert('RGBA')
    pixels = banner.load()
    for x in range(banner.width):
        for y in range(banner.height):
            r, g, b, a = pixels[x, y]
            if r < 15 and g < 15 and b < 15:
                pixels[x, y] = (r, g, b, 0)
    banner.save(f'{out_dir}/setup_header.png', 'PNG')
    print("Setup Header generated!")

# 3. Gothic Pillar (Using the new square purple column copilot_image_1784405532803(1).jpg!)
pillar_src = 'uploads/copilot_image_1784405532803(1).jpg'
if os.path.exists(pillar_src):
    img = Image.open(pillar_src).convert('RGBA')
    pixels = img.load()
    for x in range(img.width):
        for y in range(img.height):
            r, g, b, a = pixels[x, y]
            # Key out pure black background
            if r < 15 and g < 15 and b < 15:
                pixels[x, y] = (r, g, b, 0)
    img.save(f'{out_dir}/gothic_pillar.png', 'PNG')
    print("Gothic Pillar generated!")

# 3.5 Gothic Pawns (Using your grayscale 3D character statue copilot_image_1784405213506.jpeg!)
pawn_src = 'uploads/copilot_image_1784405213506.jpeg'
if os.path.exists(pawn_src):
    img = Image.open(pawn_src).convert('RGBA')
    pixels = img.load()
    for x in range(img.width):
        for y in range(img.height):
            r, g, b, a = pixels[x, y]
            # Key out pure black background
            if r < 15 and g < 15 and b < 15:
                pixels[x, y] = (r, g, b, 0)
    img.save(f'{out_dir}/gothic_pawns.png', 'PNG')
    print("Gothic Pawns (3D Character Statue) generated!")

# 4. Large & Short Gothic Frames (from uploads!)
def crop_and_flood_fill(img_path, out_path, left, top, right, bottom, threshold=25):
    img = Image.open(img_path)
    cropped = img.crop((left, top, right, bottom)).convert('RGBA')
    width, height = cropped.size
    pixels = cropped.load()
    visited = set()
    queue = []
    for x in range(width):
        queue.append((x, 0)); queue.append((x, height - 1))
        visited.add((x, 0)); visited.add((x, height - 1))
    for y in range(height):
        queue.append((0, y)); queue.append((width - 1, y))
        visited.add((0, y)); visited.add((width - 1, y))
    head = 0
    while head < len(queue):
        cx, cy = queue[head]
        head += 1
        r, g, b, a = pixels[cx, cy]
        if r < threshold and g < threshold and b < threshold:
            pixels[cx, cy] = (r, g, b, 0)
            for dx, dy in [(-1,0), (1,0), (0,-1), (0,1)]:
                nx, ny = cx + dx, cy + dy
                if 0 <= nx < width and 0 <= ny < height:
                    if (nx, ny) not in visited:
                        visited.add((nx, ny))
                        nr, ng, nb, _ = pixels[nx, ny]
                        if nr < threshold and ng < threshold and nb < threshold:
                            queue.append((nx, ny))
    cropped.save(out_path, 'PNG')

crop_and_flood_fill('uploads/copilot_image_1784396253606.jpeg', f'{out_dir}/gothic_frame_large.png', 62, 40, 1472, 943)
crop_and_flood_fill('uploads/copilot_image_1784396318996.jpeg', f'{out_dir}/gothic_frame_short.png', 72, 255, 1463, 814)
print("Frames generated!")

# 5. Buttons with double-threshold (black and white) transparency keying (from uploads!)
def double_threshold_transparency(img_path, out_path, black_thresh=45, white_thresh=180):
    img = Image.open(img_path).convert('RGBA')
    width, height = img.size
    pixels = img.load()
    for x in range(width):
        for y in range(height):
            r, g, b, a = pixels[x, y]
            if (r < black_thresh and g < black_thresh and b < black_thresh) or (r > white_thresh and g > white_thresh and b > white_thresh):
                pixels[x, y] = (r, g, b, 0)
    img.save(out_path, 'PNG')

double_threshold_transparency('uploads/copilot_image_1784395820341.jpeg', f'{out_dir}/begin_session_btn.png')
double_threshold_transparency('uploads/copilot_image_1784396722346.jpeg', f'{out_dir}/open_creation_btn.png')
print("Buttons generated with double-threshold keying!")

# 6. Gothic Runes (6x5 Grid)
runes_sheet = 'uploads/copilot_image_1784409181963.jpeg'
if os.path.exists(runes_sheet):
    img = Image.open(runes_sheet).convert('RGBA')
    pixels = img.load()
    for x in range(img.width):
        for y in range(img.height):
            r, g, b, a = pixels[x, y]
            if r < 20 and g < 20 and b < 20:
                pixels[x, y] = (r, g, b, 0)
    img.save(f'{out_dir}/gothic_runes.png', 'PNG')
    print("Gothic Runes generated!")

# 7. IC Launcher App Icon
icon_src = 'uploads/copilot_image_1784392021668.jpeg'
if os.path.exists(icon_src):
    img = Image.open(icon_src).convert('RGBA')
    resized = img.resize((512, 512), Image.Resampling.LANCZOS)
    resized.save(f'{out_dir}/ic_launcher.png', 'PNG')
    print("Launcher App Icon generated!")

# 8. Kink & Punishment Cards (Cropping fronts and backs)
kink_sheet = 'uploads/copilot_image_1784404047791.jpeg'
punish_sheet = 'uploads/copilot_image_1784408645525.jpeg'
if os.path.exists(kink_sheet) and os.path.exists(punish_sheet):
    img1 = Image.open(kink_sheet).convert('RGB')
    img2 = Image.open(punish_sheet).convert('RGB')
    w1, h1 = img1.size
    w2, h2 = img2.size
    img1.crop((0, 0, w1 // 2, h1)).save(f'{out_dir}/kink_card_back.jpg', 'JPEG')
    img1.crop((w1 // 2, 0, w1, h1)).save(f'{out_dir}/kink_card_front.jpg', 'JPEG')
    img2.crop((0, 0, w2 // 2, h2)).save(f'{out_dir}/punish_card_back.jpg', 'JPEG')
    img2.crop((w2 // 2, 0, w2, h2)).save(f'{out_dir}/punish_card_front.jpg', 'JPEG')
    print("Kink & Punishment cards generated!")

# 9. Clean up all replaced JPG files to prevent resource conflicts
to_delete = [
    'gothic_pillar.jpg', 'gothic_runes.jpg', 'ic_launcher.xml', 'setup_header.jpg',
    'gothic_frame_large.jpg', 'gothic_frame_short.jpg', 'begin_session_btn.jpg',
    'open_creation_btn.jpg'
]
for td in to_delete:
    p_td = f'{out_dir}/{td}'
    if os.path.exists(p_td):
        os.remove(p_td)
        print(f"Removed {td}")

print("--- ALL IMAGES PROCESSED SUCCESSFULLY ---")

# Author : https://github.com/boguss1225
import os
import csv
from xml.dom import minidom
import cv2
import numpy as np

# PATH TO BE CONFIGURED
CSV_PATH = "test/topview.csv"
IMG_PATH = "test/topview"
DEST_PATH = "test/topview_cropped"

# Make destin_dir
if not os.path.exists(DEST_PATH) :
    os.mkdir(DEST_PATH)

if __name__ == "__main__":
    totol_cnt = 0
    csv_file = open(CSV_PATH)
    csv_reader = csv.DictReader(csv_file, delimiter=',')
    
    # itterate csv rows
    for row in csv_reader:
        cnt = 0
        # make filename
        filename, ext = os.path.splitext(row["filename"])
        save_filename = filename+"_"+row["class"]+"_"+str(cnt)+ext

        # make dir per class
        class_dir = os.path.join(DEST_PATH,row["class"])
        if not os.path.exists(class_dir) :
            os.mkdir(class_dir)

        save_path = os.path.join(class_dir,save_filename)

        # if already has same name
        while os.path.exists(save_path) :
            cnt += 1
            save_filename = filename+"_"+row["class"]+"_"+str(cnt)+ext
            save_path = os.path.join(class_dir,save_filename)

        # target image path
        img_path_ = os.path.join(IMG_PATH,row["filename"])
        img = cv2.imread(img_path_)
             
        # Cropping an image
        cropped_image = img[int(row["ymin"]):int(row["ymax"]),
                            int(row["xmin"]):int(row["xmax"])]

        # Save the cropped image
        cv2.imwrite(save_path, cropped_image)
        totol_cnt += 1

    print(str(totol_cnt)+" files are done")
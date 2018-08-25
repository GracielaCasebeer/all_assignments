#!/usr/bin/python

import os
import sys
import json
import email
import codecs
import csv

def getMime(fname):
    f = codecs.open(fname, encoding='latin1')
    msg = email.message_from_file(f)
    f.close()
    return msg

def parseEmails(header):
    for k in ["To","Cc","Bcc"]:
        if not k in header:
            continue
        emails = []
        # clean the input
        clean = ''.join([c for c in header[k] if ord(c) < 128])
        reader = csv.reader(clean.split("\n"),delimiter=",")
        for row in reader:
            emails.extend(row)
        header[k] = [e.strip() for e in emails if "@" in e]

# check arguments
if len(sys.argv) != 3:
    print("Usage: %s <email-folder> <output-file>"%sys.argv[0])
    exit(-1)

emailFolder = sys.argv[1]
jsonFilename = sys.argv[2]
print("processing emails from %s\nwriting json to %s.\n"%(emailFolder,jsonFilename))

count = 0
out = open(jsonFilename, 'w')
for root, dirs, files in os.walk(emailFolder):
    for f in files:
        fn = os.sep.join([root,f])
        msg = getMime(fn)
        header = { k : msg[k] for k in msg.keys() }
        # examine the To, CC, and BCC headers
        parseEmails(header)
        body = msg.get_payload()
        obj = {"file" : fn, "header" : header, "body" : body }
        output = json.dumps(obj,encoding='latin1')
        out.write("%s\n"%output)
        count += 1
        if count % 1000 == 0:
           sys.stdout.write("Processed %6d files...\r"%count)
out.close()

print("Processed %6d files\n"%count)

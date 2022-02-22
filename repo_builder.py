import sys
import os

def getPatchFilesAndBugs(drrPath):
	patchFilePaths = []
	for path, subdirs, files in os.walk(drrPath):
	    for name in files:
	        patchFilePaths.append(os.path.join(path, name))
	bugs = set()
	patchFiles = set()
	for f in patchFilePaths:
		patchFile = f.split("/")[-1].split(".")[0]
		bugId = patchFile.split("-")[1] + "-" + patchFile.split("-")[2]
		bugs.add(bugId)
		patchFiles.add(patchFile)

	return (patchFilePaths, patchFiles, bugs)

def cloneBugs(bugs, datasetPath):
	for b in bugs:
		repo = b.split("-")[0]
		bugNum = b.split("-")[1]
		clonePath = f'{datasetPath}/{b}'
		os.system(f"defects4j checkout -p {repo} -v {bugNum}b -w {clonePath}")
		os.system(f'rm {clonePath}/.git -rf')

	os.chdir(f'{datasetPath}')
	os.system(f'git add --all')
	os.system(f'git commit -m "bugs added"')
	os.system(f'git push origin master')
	os.chdir(f"..")

def createPRs(datasetPath, patchFilePaths, patchFiles):
	os.chdir(f'{datasetPath}')
	for patch in patchFiles:
		patchPath = [i for i in patchFilePaths if patch in i][0]
		bugId = bugId = patch.split("-")[1] + "-" + patch.split("-")[2]
		print(patchPath, patch)
		os.system(f'git checkout -b {patch}')
		os.system(f'git apply --directory="{bugId}" {patchPath}')
		os.system(f'git add --all')
		os.system(f'git commit -m "patch applied"')
		os.system(f'git push origin {patch}')
		os.system(f'gh pr create -B master -f')
		os.system(f'git checkout master')
	os.chdir('..')

def main(argv):
	drrPath = argv[0]
	datasetPath = argv[1]
	
	patchFilePaths, patchFiles, bugs = getPatchFilesAndBugs(drrPath)
	
	cloneBugs(bugs, datasetPath)

	# createPRs(datasetPath, patchFilePaths, patchFiles)

if __name__ == "__main__":
    main(sys.argv[1:])

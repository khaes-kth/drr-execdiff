import sys
import os

slug = "khaes-kth/drr-execdiff"

def process(patchName, bugId, tests):
    os.chdir('drr-execdiff')
    os.system(f'git checkout {patchName}')
    isPatched = "patch applied" in os.popen(f'git log -1 --pretty=%B').read()
    if isPatched:
        commit = os.popen(f'git log --format="%H" -n 1').read().split('\n')[0]
        changedFiles = os.popen(f'git diff-tree --no-commit-id --name-only -r {commit}').read().split('\n')
        if len(changedFiles) == 2:
            changedFile = '/'.join(changedFiles[0].split("/")[1:])
            os.system("rm -rf ../original/")
            os.system("rm -rf ../patched/")
            os.system(f'cp -R ./Time-{bugId} ../patched')
            os.system('git checkout master')
            os.system(f'cp -R ./Time-{bugId} ../original')
            os.chdir('..')
            os.system(f"sed -i 's/1.5/1.6/' original/pom.xml")
            os.system(f"sed -i 's/1.5/1.6/' patched/pom.xml")
            os.mkdir(f'output/{patchName}')
            os.system(f'timeout 1000 java -jar explainer.jar {slug} {commit} /home/khaes/phd/projects/explanation/code/tmp/time-repo-test/original /home/khaes/phd/projects/explanation/code/tmp/time-repo-test/patched /home/khaes/phd/projects/explanation/code/tmp/time-repo-test/output/{patchName} http://example.com {changedFile} {tests} 1>> output/{patchName}/log.log 2>> output/{patchName}/err.err')
    os.chdir("/home/khaes/phd/projects/explanation/code/tmp/time-repo-test")

def main(argv):
    with open(argv[0]) as branchesFile, open(argv[1]) as testsFile:
        branches = [line.rstrip() for line in branchesFile.readlines()]
        testsLst = [line.rstrip() for line in testsFile.readlines()]
        for branch in branches:
            bugId = branch.split('-')[2]
            tests = testsLst[int(bugId)].split("\"")[-2]
            tests = ','.join(list(set([testCase.split('::')[0] for testCase in tests.split(';')])))
            patchName = branch.split('/')[1]
            process(patchName, bugId, tests)

if __name__ == "__main__":
    main(sys.argv[1:])

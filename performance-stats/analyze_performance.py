import sys
import os
import multiprocessing as mp
from os.path import exists
import time

def main(argv):
    perf_stats = []
    for i in range(0, 4):
    	depth = i
    	analyzed = os.popen(f'ls exec-diff{i}/output/logs/sahab_*.log | wc -l').read().split('\n')[0]
    	with_empty_sahab_report = os.popen(f'find exec-diff{i}/output/sahab-reports/*/right.json -type f -size -1 | wc -l').read().split('\n')[0]
    	with_not_empty_sahab_report = os.popen(f'find exec-diff{i}/output/sahab-reports/*/right.json -type f -size +0 | wc -l').read().split('\n')[0]
    	with_manipulated_diff = os.popen(f'find exec-diff{i}/output/logs/ -type f -print0 | xargs -0 grep -l "UI manipulation" | wc -l').read().split('\n')[0]
    	with_distinct_states_added = os.popen(f'find exec-diff{i}/output/state_diffs/ -type f -print0 | xargs -0 grep -l "only occurs" | wc -l').read().split('\n')[0]
    	with_diff_generated = os.popen(f'ls exec-diff{i}/output/state_diffs/* | wc -l').read().split('\n')[0]
    	perf_stats.append({'depth': depth, 'analyzed': analyzed, 'with_empty_sahab_report': with_empty_sahab_report, 'with_diff_generated': with_diff_generated,
    		'with_not_empty_sahab_report': with_not_empty_sahab_report, 'with_manipulated_diff': with_manipulated_diff, 'with_distinct_states_added': with_distinct_states_added})

    commits_with_state_diff = []
    os.system(f'ls -l exec-diff3/output/state_diffs/* > diffs.lst')
    with open('diffs.lst') as file:
        lines = file.readlines()
        lines = [line.rstrip() for line in lines]
        for line in lines:
            commits_with_state_diff.append(line.split('_')[-1].split(".")[0])

    for commit in commits_with_state_diff:
        for i in range(4):
            if not exists(f'exec-diff{i}/output/state_diffs/state_diff_{commit}.html'):
                commits_with_state_diff.remove(commit)
                break

    commits_cnt = len(commits_with_state_diff)

    for i in range(4):
        sahab_time = 0.0
        line_var_and_mapping_computation_time = 0.0
        diff_computation_time = 0.0
        ui_manipulation_time = 0.0
        for commit in commits_with_state_diff:
            with open(f'exec-diff{i}/output/logs/sahab_{commit}.log') as file:
                lines = file.readlines()
                lines = [line.rstrip() for line in lines]
                for line in lines:
                    if 'Sahab spent time' in line:
                        sahab_time += int(line.split(' ')[-1])
            
            with open(f'exec-diff{i}/output/logs/diff_computer_{commit}.err') as file:
                lines = file.readlines()
                lines = [line.rstrip() for line in lines]
                for line in lines:
                    if 'UI manipulation' in line:
                        ui_manipulation_time += int(line.split(' ')[-2])
                    if 'diff computation took' in line:
                        diff_computation_time += int(line.split(' ')[-2])
                    if 'line mappings and vars computation' in line:
                        line_var_and_mapping_computation_time += int(line.split(' ')[-2])
            
            perf_stats[i]['sahab_time'] = sahab_time / commits_cnt
            perf_stats[i]['ui_manipulation_time'] = ui_manipulation_time / commits_cnt
            perf_stats[i]['line_var_and_mapping_computation_time'] = line_var_and_mapping_computation_time / commits_cnt
            perf_stats[i]['diff_computation_time'] = diff_computation_time / commits_cnt

    print(f'Number of commits with reports: {commits_cnt}')

    print(f'depth,#analyzed,#with_empty_sahab_report,#with_not_empty_sahab_report,#with_manipulated_diff,#with_diff_generated,#with_distinct_states_added,sahab_time,line_var_and_mapping_computation_time,diff_computation_time,ui_manipulation_time')
    for i in range(4):
        print(f'{perf_stats[i]["depth"]},{perf_stats[i]["analyzed"]},{perf_stats[i]["with_empty_sahab_report"]},{perf_stats[i]["with_not_empty_sahab_report"]},{perf_stats[i]["with_manipulated_diff"]},' 
    		+ f'{perf_stats[i]["with_diff_generated"]},{perf_stats[i]["with_distinct_states_added"]},{perf_stats[i]["sahab_time"]},{perf_stats[i]["line_var_and_mapping_computation_time"]}' 
    		+ f',{perf_stats[i]["diff_computation_time"]},{perf_stats[i]["ui_manipulation_time"]}')

if __name__ == "__main__":
    main(sys.argv[1:])

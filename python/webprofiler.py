from selenium import webdriver
from selenium.webdriver.common import keys
import json
import sys
import collections
import re


RETURN = keys.Keys.RETURN

def log_filter(logs):
    allkeys = []
    entries = {}
    for l in logs:
        longentry = json.loads(l['message'])['message']
        if longentry['method']=='Network.responseReceived':
            k = longentry["params"]["requestId"]
            entry = longentry['params']['response']
            if 'requestHeadersText' in entry:
                rqst = entry['requestHeadersText']
                rqmethod = rqst[0:rqst.find('\r\n')]
                log = {
                    'timestamp': l['timestamp'],
                    'rqmethod': rqmethod,
                    'starts': entry['timing']['requestTime'],
                    'url': entry['url'],
                    'requestId': k
                }
                entries[k] = log
                allkeys.append(k)
        if longentry["method"] == "Network.loadingFinished":
            k = longentry["params"]["requestId"]
            if k in entries:
                log = entries[k]
                ends = longentry["params"]["timestamp"]
                log['ends'] = ends
                log['time'] = ( log['ends'] - log['starts'] )*1000
    for k in allkeys:
        yield entries[k]


lastelem = None
def parse(command, driver):
    global lastelem
    command = command.lstrip()
    if command[0] == "#":
        return
    rex = '([^ \r\n]*)[ \t\n]*(.*)'
    res = re.search(rex, command)
    comm = res.groups()[0].lower()
    args = res.groups()[1]
    print(">>>", comm, ": ", args)
    if comm == 'go':
        driver.get(args)
    elif comm == 'findid':
        lastelem = driver.find_element_by_id(args)
    elif comm == 'findname':
        lastelem = driver.find_element_by_name(args)
    elif comm == 'findtext':
        lastelem = driver.find_element_by_link_text(args)
    elif comm == 'click':
        lastelem.click()
    elif comm == 'type':
        escaped = False
        for c in args:
            if c == '\\':
                escaped = True
            elif c == 'n' and escaped:
                lastelem.send_keys(keys.Keys.RETURN)
            elif c == '\\' and escaped:
                lastelem.send_keys('\\')
            else:
                escaped = False
                lastelem.send_keys(c)
        


def run(suite):

    driver = webdriver.Chrome(
        executable_path="./chromedriver",
        desired_capabilities={'loggingPrefs': {'performance': 'INFO'}})


    try:
        driver.implicitly_wait(30)

        # Hack the Logging API into the Python remote driver.
        # Not implemented in Selenium, patch welcome!!
        driver.command_executor._commands.update({
            'getAvailableLogTypes': ('GET', '/session/$sessionId/log/types'),
            'getLog': ('POST', '/session/$sessionId/log')})

        # print( 'Available log types:', driver.execute('getAvailableLogTypes')['value'])

        if isinstance(suite, collections.Iterable):
            for comm in suite:
                parse(comm, driver)
        else:
            suite(driver)

        logs = driver.execute('getLog', {'type': 'performance'})['value']
        print(json.dumps(list(log_filter(logs)), indent=4))

    finally:
        driver.quit()



def main():
    print(sys.argv)
    if len(sys.argv) == 2:
        with open(sys.argv[1]) as fi:
            commands = fi.readlines()
            run(commands)


if __name__ == '__main__':
    main()


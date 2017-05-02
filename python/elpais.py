import webprofiler

def suite(driver):
    driver.get('http://www.elpais.com/')
    elem = driver.find_element_by_id('boton_buscador') # Find the search button
    elem.click()
    elem = driver.find_element_by_name('qt') # Find the query box
    elem.send_keys('psd2')
    elem.send_keys(test2.RETURN)
    elem = driver.find_element_by_link_text('Digitalización y regulación financiera')
    elem.click()


webprofiler.run(suite)

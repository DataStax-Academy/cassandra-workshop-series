import os


# basic helper to get CQL schema from schema.cql file
def get_cql_schema_string_from_file(string_key):
    cql_string = ''
    start_of_block = False
    schema_cql_file_path = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), 'schema.cql')
    with open(schema_cql_file_path, 'r') as f:
        for line in f:
            if ' ' + string_key + ' ' in line:
                cql_string += line.strip('\n').strip(' ')
                start_of_block = True
            if start_of_block is True and string_key not in line:
                cql_string += ' ' + line.strip('\n').strip(' ')
            if start_of_block is True and '\n' == line:
                break
    return cql_string

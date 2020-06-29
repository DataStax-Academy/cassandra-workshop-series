import os
import ast
import sys
import operator
import traceback


def add_os_path_to_namespace(namespace=None):
    """ convenience function to add all os.path functions to the secure
    namespace """
    if namespace is None:
        namespace = {}
    for x in dir(os.path):
        if not x.startswith('_'):
            namespace['os.path.{0}'.format(x)] = getattr(os.path, x)
    return namespace


def load(path, namespace=None):
    """ convenience function to load the file with the os.path namespace added
    """
    if namespace is None:
        namespace = {}
    add_os_path_to_namespace(namespace)
    with open(path, 'r') as store:
        config = Config(store, namespace=namespace)
    return config


class ConfigException(Exception):
    pass


class Config(dict):
    def __init__(self, filelike, namespace=None):
        if namespace is None:
            namespace = {}
        filelike.seek(0)
        lines = filelike.read().splitlines()
        filelike.seek(0)
        self._load(namespace, filelike, lines)

    def _load(self, namespace, filelike, lines):
        tree = ast.parse(filelike.read())
        evaluator = Evaluator(namespace)
        for node in tree.body:
            if isinstance(node, ast.Assign):
                key = node.targets.pop().id
                try:
                    value = evaluator.eval(node.value)
                    if key not in self:
                        self[key] = value
                    else:
                        raise ConfigException(
                            'trying to overwrite key "{0}"'.format(key))
                    # also update our namespace
                    evaluator._safe_names[key] = value
                except ConfigException as e:
                    raise e
                except Exception as e:
                    raise ConfigException(
                        'couldn\'t evaluate: {0}'.format(
                            lines[node.lineno-1]))


class Evaluator(object):
    _safe_names = {'None': None, 'True': True, 'False': False}

    def __init__(self, namespace):
        self._namespace = namespace

    def Str(self, node):
        return node.s

    def Num(self, node):
        return node.n

    def Set(self, node):
        return {self.eval(x) for x in node.elts}

    def NameConstant(self, node):
        return node.value

    def Name(self, node):
        if node.id in self._safe_names:
            return self._safe_names[node.id]
        else:
            print('not in safe namespace', node.id)

    def Tuple(self, node):
        return tuple(self.eval(x) for x in node.elts)

    def List(self, node):
        return [self.eval(x) for x in node.elts]

    def Dict(self, node):
        d = {}
        for k, v in zip(node.keys, node.values):
            d[self.eval(k)] = self.eval(v)
        return d

    def UnaryOp(self, node):
        operand = self.eval(node.operand)
        opname = node.op.__class__.__name__.lower()
        if hasattr(operator, opname):
            return getattr(operator, opname)(operand)
        elif hasattr(operator, '{0}_'.format(opname)):
            return getattr(operator, '{0}_'.format(opname))(operand)

    def BinOp(self, node):
        left = self.eval(node.left)
        right = self.eval(node.right)
        opname = node.op.__class__.__name__.lower()
        if hasattr(operator, opname):
            return getattr(operator, opname)(left, right)
        elif hasattr(operator, '{0}_'.format(opname)):
            return getattr(operator, '{0}_'.format(opname))(left, right)
        else:
            print('shit never was ment to go here')
            return node

    def _get_func(self, value, attr):
        """ get the whole function path """
        if isinstance(value, ast.Attribute):
            return self._get_func(
                value.value, '{0}.{1}'.format(value.attr, attr))
        else:
            return '{0}.{1}'.format(value.id, attr)

    def Call(self, node):
        func = self._get_func(node.func.value, node.func.attr)
        args = [self.eval(arg) for arg in node.args]
        if func in self._namespace:
            return self._namespace[func](*args)
        else:
            raise ConfigException('"{0}" is not allowed'.format(func))

    def Lambda(self, node):
        raise ConfigException('lambda not supported')

    def eval(self, node):
        try:
            meth = getattr(self, node.__class__.__name__)
            return meth(node)
        except AttributeError as e:
            raise ConfigException('"{0}" not supported'.format(node))

def wrap_str_with_quote(string: str):
    if string is None:
        return None
    return f'"{string}"'